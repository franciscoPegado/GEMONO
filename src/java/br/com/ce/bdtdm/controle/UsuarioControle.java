/*+ 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.controle;

import br.gov.ce.esp.dao.HibernateDAO;
import br.gov.ce.esp.dao.InterfaceDAO;
import br.gov.ce.esp.filter.LoginManager;
import br.gov.ce.esp.pojos.Usuarios;
import br.gov.ce.esp.util.EnviarMail;
import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import java.io.Serializable;
import javax.faces.event.ActionEvent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.hibernate.Query;
import javax.mail.MessagingException;

/**
 *
 * @author weider
 */
@ManagedBean(name = "usuarioControle")
@SessionScoped
public class UsuarioControle implements Serializable {

    private Usuarios usuarios = null;
    private Session session = null;
    private InterfaceDAO usuarioDAO = null;
    private Boolean autenticado = false;
    private Transaction tx = null;
    private String senhaAtual = null;
    private String senha = null;
    private String confSenha = null;
    private Boolean botaoAlterarSenha = false;

    public UsuarioControle() {
        usuarios = new Usuarios();
        usuarioDAO = new HibernateDAO();
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean getAutenticado() {
        return autenticado;
    }

    public String getSenhaAtual() {
        return senhaAtual;
    }

    public void setSenhaAtual(String senhaAtual) {
        this.senhaAtual = senhaAtual;
    }

    public String getConfSenha() {
        return confSenha;
    }

    public void setConfSenha(String confSenha) {
        this.confSenha = confSenha;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getBotaoAlterarSenha() {
        return botaoAlterarSenha;
    }

    public void setBotaoAlterarSenha(Boolean botaoAlterarSenha) {
        this.botaoAlterarSenha = botaoAlterarSenha;
    }

    public void incluir(ActionEvent actionEvent) {
        if (validaMail(usuarios.getEmail().toLowerCase())) {
            if (usuarios.getSenha().equals(usuarios.getConfSenha())) {
                if (usuarios.getCodigo() == 0) {
                    try {
                        session = HibernateUtil.getSessionFactory().openSession();
                        tx = session.beginTransaction();
                        Query q = session.createQuery("from Usuarios u where u.cpf = :cpf");
                        //add o parametro a consulta
                        q.setParameter("cpf", usuarios.getCpf());
                        //add a consulta a lista
                        List<Usuarios> listaUsuarios = q.list();
                        if (listaUsuarios.isEmpty()) {
                            usuarios = new Usuarios(usuarios.getEmail(), usuarios.getNome(), usuarios.getSenha(), usuarios.getCpf());
                            session.save(usuarios);
                            tx.commit();
                            FacesUtils.mostrarMensagem("Usuário incluído com sucesso!");
                            usuarios = new Usuarios();
                        } else {
                            FacesUtils.mostrarMensagem("CPF já cadastrado no sistema!");
                        }
                    } catch (HibernateException hbe) {
                        FacesUtils.mostrarMensagem("Erro ao salvar os dados " + hbe);
                    } finally {
                        if (session != null) {
                            session.close();
                        }
                    }
                }
            } else {
                FacesUtils.mostrarMensagem("Senha não confere!");
            }
        } else {
            FacesUtils.mostrarMensagem("E-mail inválido!");
        }
    }

    public String Login() {
        //Cria a sessao com o banco
        session = HibernateUtil.getSessionFactory().openSession();
        //cria a query HQL
        Query q = session.createQuery("from Usuarios u where u.cpf = :cpf and u.senha = :senha");
        //add o parametro a consulta
        q.setParameter("cpf", usuarios.getCpf());
        q.setParameter("senha", usuarios.getSenha());
        //add a consulta a lista
        List<Usuarios> listaUsuarios = q.list();
        if (listaUsuarios != null && listaUsuarios.size() > 0) {
            usuarios = listaUsuarios.get(0);
            if (usuarios.getCpf() != null && usuarios.getSenha().equals(usuarios.getSenha())) {
                autenticado = true;
                setBotaoAlterarSenha(true);
                insereSession(usuarios);
                return "/publico/frmDownMono.xhtml";
            }
        }
        FacesUtils.mostrarMensagem("Erro de Autenticação!!!");
        usuarios = new Usuarios();
        return null;
    }

    private void insereSession(Usuarios usuario) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(LoginManager.SESSION_USU, usuario.getNome());
    }

    public String confirmar() {
        if (usuarios.getCodigo() == 0) {
            this.usuarioDAO.inserir(usuarios);
            usuarios = new Usuarios();
        } else {
            this.usuarioDAO.atualizar(usuarios);
            usuarios = new Usuarios();
        }
        return null;
    }

    public void cancelarLogin(ActionEvent actionEvent) {
        usuarios = new Usuarios();
    }

    public String logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "/faces/index.xhtml";
    }

    public void recuSenha() throws MessagingException {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
        //cria a query HQL
        Query q = session.createQuery("from Usuarios u where u.cpf = :cpf");
        q.setParameter("cpf", usuarios.getCpf());
        List<Usuarios> listaUsuarios = q.list();
        if (listaUsuarios != null && listaUsuarios.size() > 0) {
            usuarios = listaUsuarios.get(0);
            usuarios.setSenha("123456");
            session.update(usuarios);
            tx.commit();
            validaMail(usuarios.getEmail());
            EnviarMail mail = new EnviarMail();
            mail.sendMail(usuarios.getEmail(), usuarios.getNome());
            FacesUtils.mostrarMensagem("Uma nova senha foi enviada para seu email!");
        } else {
            FacesUtils.mostrarMensagem("CPF não cadastrado!!");
        }
        usuarios = new Usuarios();
        session.close();
    }

    public void alterarSenha() {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
        //cria a query HQL
        Query q = session.createQuery("from Usuarios u where u.cpf = :cpf");
        //add o parametro a consulta
        q.setParameter("cpf", usuarios.getCpf());
        //add a consulta a lista
        List<Usuarios> listaUsuarios = q.list();
        usuarios = listaUsuarios.get(0);
        if (usuarios.getSenha().equals(senhaAtual)) {
            if (senha.equals(confSenha)) {
                usuarios.setSenha(senha);
                session.update(usuarios);
                tx.commit();
                FacesUtils.mostrarMensagem("Senha alterada com sucesso!");
                senha = "";
                senhaAtual = "";
                confSenha = "";
            } else {
                FacesUtils.mostrarMensagem("Senha não confere!");
            }
        } else {
            FacesUtils.mostrarMensagem("Senha atual não confere com a senha cadastrada!");
        }
        session.close();
    }

    public Boolean validaMail(String email) {
        String enteredEmail = email;
        Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher m = p.matcher(enteredEmail);
        boolean matchFound = m.matches();
        if (!matchFound) {
            return false;
        } else {
            return true;
        }
    }
}
