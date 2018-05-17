/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.controle;

import br.gov.ce.esp.pojos.Linhaspesquisa;
import br.gov.ce.esp.pojos.Monografias;
import br.gov.ce.esp.pojos.Programas;
import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.faces.event.ActionEvent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.hibernate.Query;

/**
 *
 * @author francisco
 */
@ManagedBean(name = "monografiaControle")
@SessionScoped
public class MonografiasControle {

    private Monografias monografias = null;
    private Linhaspesquisa linhasPesquisa = null;
    private Programas programas = null;
    private Session session = null;
    private Transaction tx = null;
    private DataModel model = null;
    private HtmlSelectOneMenu htmlSelectOneMenu = null;
    private String nomeArquivoSelecionado = null;
    private String dirAbsoluto = "C:/Users/francisco/Desktop/gemono1/web/monografias/";
    private String desc = "";
    private String autor = "";
    private String ori = "";
    private String coori = "";
    private String bancaExa = "";
    private String assunto = "";
    private Date datDefIni = null;
    private Date datDefFim = null;
    private boolean exibirFormPesq = false;
    private boolean exibirFormPesqAvanc = false;
    private boolean pesqMonoAberto = false;
    private boolean datasValidas = true;
    private HtmlSelectOneMenu selectOneMenuLinhasPesquisas = null;
    private HtmlSelectOneMenu selectOneMenuPrograma = null;
    private int codLinPesq;
    private int codProg;

    public MonografiasControle() {
        monografias = new Monografias();
        linhasPesquisa = new Linhaspesquisa();
        programas = new Programas();
    }

    public Programas getProgramas() {
        return programas;
    }

    public void setProgramas(Programas programas) {
        this.programas = programas;
    }

    public Linhaspesquisa getLinhasPesquisa() {
        return linhasPesquisa;
    }

    public void setLinhasPesquisa(Linhaspesquisa linhasPesquisa) {
        this.linhasPesquisa = linhasPesquisa;
    }

    public Monografias getMonografias() {
        return monografias;
    }

    public void setMonografias(Monografias monografias) {
        this.monografias = monografias;
    }

    public DataModel getModel() {
        if (pesqMonoAberto == true) {
            if (exibirFormPesq == true) {
                return model = new ListDataModel(PesquisaMonografias());
            } else {
                return model = new ListDataModel();
            }
        } else {
            if (exibirFormPesqAvanc == true) {
                return model = new ListDataModel(PesquisaMonografiasAvanc());
            } else {
                return model = new ListDataModel();
            }
        }
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public HtmlSelectOneMenu getHtmlSelectOneMenu() {
        return htmlSelectOneMenu;
    }

    public void setHtmlSelectOneMenu(HtmlSelectOneMenu htmlSelectOneMenu) {
        this.htmlSelectOneMenu = htmlSelectOneMenu;
    }

    public String getNomeArquivoSelecionado() {
        return nomeArquivoSelecionado;
    }

    public void setNomeArquivoSelecionado(String nomeArquivoSelecionado) {
        this.nomeArquivoSelecionado = nomeArquivoSelecionado;
    }

    public boolean isExibirFormPesq() {
        return exibirFormPesq;
    }

    public void setExibirFormPesq(boolean exibirForm) {
        this.exibirFormPesq = exibirForm;
    }

    public boolean isExibirFormPesqAvanc() {
        return exibirFormPesqAvanc;
    }

    public void setExibirFormPesqAvanc(boolean exibirFormPesqAvanc) {
        this.exibirFormPesqAvanc = exibirFormPesqAvanc;
    }

    public HtmlSelectOneMenu getSelectOneMenuLinhasPesquisas() {
        return selectOneMenuLinhasPesquisas;
    }

    public void setSelectOneMenuLinhasPesquisas(HtmlSelectOneMenu selectOneMenuLinhasPesquisas) {
        this.selectOneMenuLinhasPesquisas = selectOneMenuLinhasPesquisas;
    }

    public HtmlSelectOneMenu getSelectOneMenuPrograma() {
        return selectOneMenuPrograma;
    }

    public void setSelectOneMenuPrograma(HtmlSelectOneMenu selectOneMenuPrograma) {
        this.selectOneMenuPrograma = selectOneMenuPrograma;
    }
    //-----------------------------------------------------------------------------------------------------

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getBancaExa() {
        return bancaExa;
    }

    public void setBancaExa(String bancaExa) {
        this.bancaExa = bancaExa;
    }

    public String getCoori() {
        return coori;
    }

    public void setCoori(String coori) {
        this.coori = coori;
    }

    public Date getDatDefIni() {
        return datDefIni;
    }

    public void setDatDefIni(Date datDef) {
        this.datDefIni = datDef;
    }

    public Date getDatDefFim() {
        return datDefFim;
    }

    public void setDatDefFim(Date datDefFim) {
        this.datDefFim = datDefFim;
    }

    public String getOri() {
        return ori;
    }

    public void setOri(String ori) {
        this.ori = ori;
    }

    public boolean isPesqMonoAberto() {
        return pesqMonoAberto;
    }

    public void setPesqMonoAberto(boolean pesqMonoAberto) {
        this.pesqMonoAberto = pesqMonoAberto;
    }

    public int getCodLinPesq() {
        return codLinPesq;
    }

    public void setCodLinPesq(int codLinPesq) {
        this.codLinPesq = codLinPesq;
    }

    public int getCodProg() {
        return codProg;
    }

    public void setCodProg(int codProg) {
        this.codProg = codProg;
    }
    //-------------------------------------------------------------------------------------------------------

    public Monografias getProdutoEditarExcluir() {
        monografias = (Monografias) model.getRowData();
        return monografias;
    }

    public String selectedRowMonografia() {
        monografias = (Monografias) model.getRowData();
        return "/faces/publico/frmExibirDown.xhtml";
    }

    public String prepararAdicionarMonografia() {
        monografias = new Monografias();
        return "/faces/adm/frmMono.xhtml";
    }

    public void incluir(ActionEvent actionEvent) {
        if (monografias.getCodigo() == 0) {
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.beginTransaction();
                monografias = new Monografias(linhasPesquisa, programas, monografias.getTlptbr().toLowerCase(), monografias.getTlpeng().toLowerCase(), monografias.getAutor().toLowerCase(),
                        monografias.getOrientador().toLowerCase(), monografias.getCoorientador().toLowerCase(), monografias.getBncex().toLowerCase(), monografias.getDatadefesa(),
                        monografias.getResumo().toLowerCase(), monografias.getAbstractresumo().toLowerCase(), monografias.getCaminho());
                session.save(monografias);
                tx.commit();
                FacesUtils.mostrarMensagem("Monografia incluída com sucesso!");
            } catch (HibernateException hbe) {
                FacesUtils.mostrarMensagem("Erro ao salvar os dados " + hbe);
            } finally {
                if (session != null) {
                    session.close();
                    monografias = new Monografias();
                    programas = new Programas();
                    linhasPesquisa = new Linhaspesquisa();
                }
            }
        }
    }

    public void excluir(ActionEvent actionEvent) {
        try {
            deletaArq();
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            monografias = getProdutoEditarExcluir();
            session.delete(monografias);
            tx.commit();
            FacesUtils.mostrarMensagem("Monografia excluída com sucesso!");
            session.close();
            monografias = new Monografias();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao excluir a monografia" + ex);
        }
    }

    public void excluirArq(ActionEvent actionEvent) {
        try {
            deletaArq();
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            monografias = getProdutoEditarExcluir();
            if (!monografias.getCaminho().equals("")) {
                monografias.setCaminho("");
                session.update(monografias);
                tx.commit();
                FacesUtils.mostrarMensagem("Arquivo excluído com sucesso!");
                session.close();
                monografias = new Monografias();
            }
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao excluir o arquivo" + ex);
        }
    }

    public void alterar(ActionEvent action) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            session.update(monografias);
            //comita a transação
            tx.commit();
            FacesUtils.mostrarMensagem("Monografia atualizada com sucesso!");
            //limpa o objeto para novas inserções
            monografias = new Monografias();
            session.close();
        } catch (RuntimeException ex) {
            tx.rollback();
            FacesUtils.mostrarMensagem("Erro ao atualizar os dados!");
        }
    }

    public void fileUploadAction(FileUploadEvent event) {
        if (event.getFile().getFileName().toLowerCase().endsWith(".pdf")) {
            try {
                setNomeArquivoSelecionado(event.getFile().getFileName());
                UploadedFile arq = event.getFile();
                InputStream in = new BufferedInputStream(arq.getInputstream());
                File file = new File(dirAbsoluto + monografias.getCodigo());
                if (file.exists()) {
                    deletaDir(file);
                }
                file.mkdirs();
                File file1 = new File(file, event.getFile().getFileName());
                monografias.setCaminho(file1.getName());
                FileOutputStream fout = new FileOutputStream(file1);
                while (in.available() != 0) {
                    fout.write(in.read());
                }
                fout.close();
                if (getNomeArquivoSelecionado() != null) {
                    session = HibernateUtil.getSessionFactory().openSession();
                    tx = session.beginTransaction();
                    session.update(monografias);
                    tx.commit();
                    setNomeArquivoSelecionado(null);
                    FacesUtils.mostrarMensagem("Arquivo salvo com sucesso!");
                    session.close();
                    monografias = new Monografias();
                }
            } catch (Exception ex) {
                FacesUtils.mostrarMensagem("Erro ao salvar o arquivo!");
            }
        } else {
            FacesUtils.mostrarMensagem("Erro: O Arquivo deve estar no formato pdf!");
        }
    }

    public void abrirArquivo() throws IOException {
        try {
            String dir = "C:/Users/francisco/Desktop/gemono1/web/monografias/" + monografias.getCodigo();
            String nomeArq = "";
            File file = new File(dir);
            if (file.exists()) {
                File afile[] = file.listFiles();
                int i = 0;
                for (int j = afile.length; i < j; i++) {
                    File arquivos = afile[i];
                    nomeArq = arquivos.getName();
                }
                File pdfFile = new File(dir + "/" + nomeArq);
                java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                desktop.open(pdfFile);
            } else {
                FacesUtils.mostrarMensagem("Este autor não possui monografia cadastrado em nosso banco de dados!");
            }
        } catch (Exception ex) {
        }
    }

    public void deletaArq() {
        File file = null;
        monografias = (Monografias) (model.getRowData());
        file = new File(dirAbsoluto + monografias.getCodigo());
        if (file.exists()) {
            deletaDir(file);
            file = new File(dirAbsoluto + monografias.getCodigo());
            file.delete();
        }
    }

    public static boolean deletaDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                // Deleta os arquivos de dentro do diretório.
                boolean success = deletaDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // Agora o diretório está vazio, restando apenas deletá-lo.
        return dir.delete();
    }

    public List<SelectItem> getListarPesquisa() {
        List<Linhaspesquisa> arrayLinhasPesquisa = null;
        List<SelectItem> listaLinhasPesquisa = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            Query q = session.createQuery("from Linhaspesquisa order by nome");
            arrayLinhasPesquisa = q.list();
            listaLinhasPesquisa = new ArrayList<SelectItem>();
            listaLinhasPesquisa.add(new SelectItem("", "--Selecione--"));
            for (Linhaspesquisa item : arrayLinhasPesquisa) {
                listaLinhasPesquisa.add(new SelectItem(item.getCodigo(), item.getNome()));
            }
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao listar" + "\n" + ex);
        }
        return listaLinhasPesquisa;
    }

    public List<SelectItem> getListarPrograma() {
        List<Programas> arrayPrograma = null;
        List<SelectItem> listaPrograma = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            tx.setTimeout(5);
            Query q = session.createQuery("from Programas order by nomeprograma");
            arrayPrograma = q.list();
            listaPrograma = new ArrayList<SelectItem>();
            listaPrograma.add(new SelectItem("", "--Selecione--"));
            for (Programas itemPrograma : arrayPrograma) {
                listaPrograma.add(new SelectItem(itemPrograma.getCodigo(), itemPrograma.getNomeprograma()));
            }
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro ao listar" + "\n" + ex);
        }
        return listaPrograma;
    }

    public DataModel<Monografias> getListarMonografias() {
        List<Monografias> arrayMonografias = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("from Monografias order by autor");
            arrayMonografias = q.list();
            session.close();
        } catch (HibernateException ex) {
            FacesUtils.mostrarMensagem("Erro" + "\n" + ex);
        }
        return model = new ListDataModel(arrayMonografias);
    }

    public List<Monografias> PesquisaMonografias() {
        pesqMonoAberto = true;
        List<Monografias> list = null;
        if (desc.equals("")) {
            list = procurarTodasMonografias();
        } else {
            list = listaMonografiasPorTitulo(desc.toLowerCase());
        }
        setExibirFormPesq(true);
        programas = new Programas();
        linhasPesquisa = new Linhaspesquisa();
        return list;
    }

    public List<Monografias> PesquisaMonografiasAvanc() {
        validaDatas();
        if (datasValidas) {
            setExibirFormPesqAvanc(true);
            pesqMonoAberto = false;
            List<Monografias> list = null;
            if (codLinPesq == 0 && codProg == 0 && desc.equals("") && autor.equals("") && ori.equals("") && coori.equals("") && bancaExa.equals("") && datDefIni == null && datDefFim == null && assunto.equals("")) {
                list = procurarTodasMonografias();
            } else {
                list = listaMonografias(codLinPesq, codProg , desc.toLowerCase(), autor.toLowerCase(), ori.toLowerCase(), coori.toLowerCase(), bancaExa.toLowerCase(), datDefIni, datDefFim, assunto.toLowerCase());
            }
            programas = new Programas();
            linhasPesquisa = new Linhaspesquisa();
            return list;
        } else {
            setExibirFormPesqAvanc(false);
            return null;
        }
    }

    public void validaDatas() {
        if (datDefIni != null || datDefFim != null) {
            if (datDefIni != null && datDefFim == null) {
                FacesUtils.mostrarMensagem("Preencha data final!");
                datasValidas = false;
                return;
            } else {
                if (datDefIni == null && datDefFim != null) {
                    FacesUtils.mostrarMensagem("Preencha data inicial!");
                    datasValidas = false;
                    return;
                } else {
                    if (datDefFim.before(datDefIni)) {
                        FacesUtils.mostrarMensagem("Data final deve ser maior que data inicial!");
                        datasValidas = false;
                        return;
                    }
                }
            }
        }
        datasValidas = true;
    }

    public List<Monografias> procurarTodasMonografias() {
        List<Monografias> list = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            list = session.createQuery("from Monografias order by autor").list();
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return list;
    }

    public List<Monografias> listaMonografiasPorTitulo(String titulo) {
        //Cria a sessao com o banco
        session = HibernateUtil.getSessionFactory().openSession();
        //cria a query HQL
        Query q = session.createQuery("from Monografias m where m.tlptbr like :titulo");
        //add o parametro de consulta
        q.setParameter("titulo", "%" + titulo + "%");
        //executa o HQL e o retorno vai para a array
        List<Monografias> listaMonografiasTitulo = q.list();
        return listaMonografiasTitulo;
    }

    public List<Monografias> listaMonografias(int codLinPesq, int codProg , String titulo, String autor, String ori, String coori, String bancaExa, Date datDefIni, Date datDefFim, String resumo) {
        List<Monografias> listaMonografiasTitulo = null;
        try {
            String query = "from Monografias m where";
            if (!titulo.equals("")) {
                query = query + " m.tlptbr like :titulo";
            } else {
                query = query + " 1=1";
            }
            if (codLinPesq != 0) {
                query = query + " and m.linhaspesquisa.codigo = :fkcodpesquisa";
            }
            if (codProg != 0) {
                query = query + " and m.programas.codigo = :fkcodprograma";
            }
            if (!autor.equals("")) {
                query = query + " and m.autor like :autor";
            }
            if (!ori.equals("")) {
                query = query + " and m.orientador like :orientador";
            }
            if (!coori.equals("")) {
                query = query + " and m.coorientador like :coorientador";
            }
            if (!bancaExa.equals("")) {
                query = query + " and m.bncex like :bncex";
            }
            if (datDefIni != null && datDefFim != null) {
                query = query + " and m.datadefesa between :datDefIni and :datDefFim";
            }
            if (!resumo.equals("")) {
                query = query + " and m.resumo like :resumo";
            }
            //Cria a sessao com o banco
            session = HibernateUtil.getSessionFactory().openSession();
            //cria a query HQL
            Query q = session.createQuery(query);
            //add o parametro de consulta
            if (!titulo.equals("")) {
                q.setParameter("titulo", "%" + titulo + "%");
            }
            if (codLinPesq != 0) {
                q.setParameter("fkcodpesquisa", codLinPesq);
            }
             if (codProg != 0) {
                q.setParameter("fkcodprograma", codProg);
            }
            if (!autor.equals("")) {
                q.setParameter("autor", "%" + autor + "%");
            }
            if (!ori.equals("")) {
                q.setParameter("orientador", "%" + ori + "%");
            }
            if (!coori.equals("")) {
                q.setParameter("coorientador", "%" + coori + "%");
            }
            if (!bancaExa.equals("")) {
                q.setParameter("bncex", "%" + bancaExa + "%");
            }
            if (datDefIni != null && datDefFim != null) {
                q.setParameter("datDefIni", datDefIni);
                q.setParameter("datDefFim", datDefFim);
            }
            if (!resumo.equals("")) {
                q.setParameter("resumo", "%" + resumo + "%");
            }
            //executa o HQL e o retorno vai para a array
            listaMonografiasTitulo = q.list();
        } catch (HibernateException e) {
            FacesUtils.mostrarMensagem("Erro na procura!");
        } finally {
            session.close();
        }
        return listaMonografiasTitulo;
    }

    public void prepararAlterar(ActionEvent actionEvent) {
        //captura a linha da tabela, fazendo um cast no objeto
        monografias = (Monografias) (model.getRowData());
        // atualiza o produto com o produto selecionado e depois atualiza o combo
        selectOneMenuLinhasPesquisas.setValue(monografias.getLinhaspesquisa().getCodigo());
        selectOneMenuLinhasPesquisas.setSubmittedValue(monografias.getLinhaspesquisa().getCodigo());
        // se nao colocar a linha abaixo, após alterar o valor, o evento de value change do combo é disparado!!!!
        selectOneMenuLinhasPesquisas.setLocalValueSet(false);

        // atualiza o produto com o produto selecionado e depois atualiza o combo
        selectOneMenuPrograma.setValue(monografias.getProgramas().getCodigo());
        selectOneMenuPrograma.setSubmittedValue(monografias.getProgramas().getCodigo());
        // se nao colocar a linha abaixo, após alterar o valor, o evento de value change do combo é disparado!!!!
        selectOneMenuPrograma.setLocalValueSet(false);
    }
}
