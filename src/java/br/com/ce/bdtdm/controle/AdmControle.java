/*+
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.controle;

import br.gov.ce.esp.dao.HibernateDAO;
import br.gov.ce.esp.dao.InterfaceDAO;
import br.gov.ce.esp.filter.LoginManager;
import br.gov.ce.esp.pojos.Adm;
import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import java.io.Serializable;
import javax.faces.event.ActionEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.servlet.http.HttpSession;
import org.hibernate.HibernateException;
import org.hibernate.Query;

/**
 *
 * @author weider
 */
@ManagedBean(name = "admControle")
@SessionScoped
public class AdmControle implements Serializable {

    private Adm adm = null;
    private Adm admNovo = null;
    private Session session = null;
    private InterfaceDAO admDAO = null;
    private Boolean autenticado = false;
    private Transaction tx = null;
    private String senhaAtual = null;
    private String senha = null;
    private String confSenha = null;
    private Boolean menuUsuarios;
    private DataModel model = null;
    private String senhaAntiga = null;
    private Boolean botaoAlterarSenhaAdmin = false;

    public AdmControle() {
        adm = new Adm();
        admNovo = new Adm();
        admDAO = new HibernateDAO();
    }

    public Adm getAdm() {
        return adm;
    }

    public void setAdm(Adm adm) {
        this.adm = adm;
    }

    public Adm getAdmNovo() {
        return admNovo;
    }

    public void setAdmNovo(Adm admNovo) {
        this.admNovo = admNovo;
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

    public Boolean getMenuUsuarios() {
        return menuUsuarios;
    }

    public void setMenuUsuarios(Boolean menuUsuarios) {
        this.menuUsuarios = menuUsuarios;
    }

    public DataModel getModel() {
        return model;
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public Boolean getBotaoAlterarSenhaAdmin() {
        return botaoAlterarSenhaAdmin;
    }

    public void setBotaoAlterarSenhaAdmin(Boolean botaoAlterarSenhaAdmin) {
        this.botaoAlterarSenhaAdmin = botaoAlterarSenhaAdmin;
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

    public String Login() {
        //Cria a sessao com o banco
        session = HibernateUtil.getSessionFactory().openSession();
        //cria a query HQL
        Query q = session.createQuery("from Adm u where u.usuario = :usuario and u.senha = :senha");
        //add o parametro a consulta
        q.setParameter("usuario", adm.getUsuario());
        q.setParameter("senha", adm.getSenha());
        //add a consulta a lista
        List<Adm> listaAdm = q.list();
        if (listaAdm != null && listaAdm.size() > 0) {
            adm = listaAdm.get(0);
            if (adm.getUsuario() != null && adm.getSenha().equals(adm.getSenha())) {
                autenticado = true;
                insereSession(adm);
                if (adm.isFlgadmin() == true) {
                    setMenuUsuarios(true);
                } else {
                    setBotaoAlterarSenhaAdmin(true);
                }
                autenticado = true;
                insereSession(adm);
                return "/adm/principal.xhtml";
            }
        }
        FacesUtils.mostrarMensagem("Erro de Autenticação!!!");
        adm = new Adm();
        return null;
    }

    public void incluir(ActionEvent actionEvent) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Query q = session.createQuery("from Adm u where u.usuario = :usuario");
            //add o parametro a consulta
            q.setParameter("usuario", admNovo.getUsuario());
            List<Adm> listaAdm = q.list();
            if (listaAdm.isEmpty()) {
                admNovo = new Adm(admNovo.getUsuario(), admNovo.getSenha(), admNovo.isFlgadmin());
                session.save(admNovo);
                tx.commit();
                FacesUtils.mostrarMensagem("Usuário incluído com sucesso!");
                admNovo = new Adm();
            } else {
                FacesUtils.mostrarMensagem("Usuário já cadastrado no sistema!");
            }
        } catch (HibernateException hbe) {
            FacesUtils.mostrarMensagem("Erro ao salvar os dados " + hbe);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public void alterar(ActionEvent action) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            if (admNovo.getSenha().equals("")) {
                admNovo.setSenha(senhaAntiga);
            }
            session.update(admNovo);
            //comita a transação
            tx.commit();
            FacesUtils.mostrarMensagem("Usuário alterardo com sucesso!");
            //limpa o objeto para novas inserções
            admNovo = new Adm();
            session.close();
        } catch (RuntimeException ex) {
            tx.rollback();
            FacesUtils.mostrarMensagem("Erro ao alterar os dados!");
        }
    }

    public void excluir(ActionEvent actionEvent) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            admNovo = (Adm) model.getRowData();
            session.delete(admNovo);
            tx.commit();
            FacesUtils.mostrarMensagem("Usuário excluído com sucesso!");
            session.close();
            admNovo = new Adm();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao excluir o usuário" + ex);
        }
    }

    public void alterarSenha() {
        session = HibernateUtil.getSessionFactory().openSession();
        tx = session.beginTransaction();
        //cria a query HQL
        Query q = session.createQuery("from Adm u where u.usuario = :usuario");
        //add o parametro a consulta
        q.setParameter("usuario", adm.getUsuario());
        //add a consulta a lista
        List<Adm> listaAdm = q.list();
        adm = listaAdm.get(0);
        if (adm.getSenha().equals(senhaAtual)) {
            if (senha.equals(confSenha)) {
                adm.setSenha(senha);
                session.update(adm);
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

    public DataModel<Adm> getListarAdm() {
        List<Adm> arrayAdm = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Adm order by usuario");
            arrayAdm = q.list();
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro" + "\n" + ex);
        }
        return model = new ListDataModel(arrayAdm);
    }

    private void insereSession(Adm adm) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        session.setAttribute(LoginManager.SESSION_USU, adm.getUsuario());
    }

    public void cancelarLogin(ActionEvent actionEvent) {
        adm = new Adm();
    }

    public String logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        return "/faces/index.xhtml";
    }

    public void selectedRowUsua(ActionEvent actionEvent) {
        //captura a linha da tabela, fazendo um cast no objeto
        admNovo = (Adm) (model.getRowData());
        senhaAntiga = admNovo.getSenha();
    }
}
