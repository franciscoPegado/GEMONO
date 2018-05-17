/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.controle;

import br.gov.ce.esp.pojos.Linhaspesquisa;
import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import javafx.event.ActionEvent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.hibernate.Query;

/**
 *
 * @author francisco
 */
@ManagedBean(name = "linhaPesquisaControle")
@SessionScoped
public class LinhaPesquisaControle {

    private Linhaspesquisa linhasPesquisa = null;
    private Session session = null;
    private Transaction tx = null;
    private DataModel model = null;

    public LinhaPesquisaControle() {
        linhasPesquisa = new Linhaspesquisa();
    }

    public Linhaspesquisa getLinhasPesquisa() {
        return linhasPesquisa;
    }

    public void setLinhasPesquisa(Linhaspesquisa linhasPesquisa) {
        this.linhasPesquisa = linhasPesquisa;
    }

    public DataModel getModel() {
        return model = new ListDataModel();
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public Linhaspesquisa getProdutoEditarExcluir() {
        linhasPesquisa = (Linhaspesquisa) model.getRowData();
        return linhasPesquisa;
    }

    public void prepararAdicionarLinhaPesquisa(ActionEvent actionEvent) {
        linhasPesquisa = new Linhaspesquisa();
    }

    public void incluir(ActionEvent actionEvent) {
        if (linhasPesquisa.getCodigo() == 0) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                linhasPesquisa = new Linhaspesquisa(linhasPesquisa.getNome());
                session.save(linhasPesquisa);
                tx.commit();
                FacesUtils.mostrarMensagem("Linha de Pesquisa incluída com sucesso!");
            } catch (HibernateException hbe) {
                FacesUtils.mostrarMensagem("Erro ao salvar os dados " + hbe);
            } finally {
                if (session != null) {
                    session.close();
                    linhasPesquisa = new Linhaspesquisa();
                }
            }
        }
    }

    public void excluir(ActionEvent actionEvent) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            linhasPesquisa = getProdutoEditarExcluir();
            session.delete(linhasPesquisa);
            tx.commit();
            FacesUtils.mostrarMensagem("Linha de Pesquisa excluída com sucesso!");
            session.close();
            linhasPesquisa = new Linhaspesquisa();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao excluir a Linha de Pesquisa" + ex);
        }
    }

    public void atualizar(ActionEvent action) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            //passa os valores para a inserção
            linhasPesquisa = new Linhaspesquisa(linhasPesquisa.getNome());
            // produtos = (Produtos) model.getRowData();
            //persiste o objeto no banco
            session.update(linhasPesquisa);
            //comita a transação
            tx.commit();
            FacesUtils.mostrarMensagem("Dados atualizados com sucesso");
            //limpa o objeto para novas inserções
            linhasPesquisa = new Linhaspesquisa();
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

    public DataModel<Linhaspesquisa> getListarLinhaPesquisa() {
        List<Linhaspesquisa> arrayLinhaPesquisa = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Linhaspesquisa order by nome");
            arrayLinhaPesquisa = q.list();
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro" + "\n" + ex);
        }
        return model = new ListDataModel(arrayLinhaPesquisa);
    }
}
