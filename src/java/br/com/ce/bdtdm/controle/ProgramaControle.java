/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.controle;

import br.gov.ce.esp.pojos.Programas;
import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import java.util.List;
import javafx.event.ActionEvent;
import javax.annotation.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.management.Query;
import javax.websocket.Session;

/**
 *
 * @author francisco
 */
@ManagedBean(name = "programaControle")
@SessionScoped
public class ProgramaControle {

    private Programas programas = null;
    private Session session = null;
    private Transaction tx = null;
    private DataModel model = null;

    public ProgramaControle() {
        programas = new Programas();
    }

    public Programas getProgramas() {
        return programas;
    }

    public void setLinhasPesquisa(Programas Programas) {
        this.programas = Programas;
    }

    public DataModel getModel() {
        return model = new ListDataModel();
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public Programas getProdutoEditarExcluir() {
        programas = (Programas) model.getRowData();
        return programas;
    }

    public void prepararAdicionarLinhaPesquisa(ActionEvent actionEvent) {
        programas = new Programas();
    }

    public void incluir(ActionEvent actionEvent) {
        if (programas.getCodigo() == 0) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                programas = new Programas(programas.getNomeprograma());
                session.save(programas);
                tx.commit();
                FacesUtils.mostrarMensagem("Programa incluído com sucesso!");
            } catch (HibernateException hbe) {
                FacesUtils.mostrarMensagem("Erro ao salvar os dados " + hbe);
            } finally {
                if (session != null) {
                    session.close();
                    programas = new Programas();
                }
            }
        }
    }

    public void excluir(ActionEvent actionEvent) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            programas = getProdutoEditarExcluir();
            session.delete(programas);
            tx.commit();
            FacesUtils.mostrarMensagem("Programa excluído com sucesso!");
            session.close();
            programas = new Programas();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao excluir o programa" + ex);
        }
    }

    public void atualizar(ActionEvent action) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            //passa os valores para a inserção
            programas = new Programas(programas.getNomeprograma());
            // produtos = (Produtos) model.getRowData();
            //persiste o objeto no banco
            session.update(programas);
            //comita a transação
            tx.commit();
            FacesUtils.mostrarMensagem("Dados atualizados com sucesso");
            //limpa o objeto para novas inserções
            programas = new Programas();
        } catch (RuntimeException ex) {
            try {
                tx.rollback();
            } catch (RuntimeException rex) {
                FacesUtils.mostrarMensagem("Não foi executado o rollback da transação");
            }
            throw ex;
        } finally {
            //fecha a sessão com o banco de dados
            if (session != null) {
                session.close();
            }
        }
    }

    public DataModel<Programas> getListarProgramas() {
        List<Programas> arrayProgramas = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Programas order by nomeprograma");
            arrayProgramas = q.list();
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro" + "\n" + ex);
        }
        return model = new ListDataModel(arrayProgramas);
    }
}
