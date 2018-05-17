package br.gov.ce.esp.pojos;
// Generated 25/08/2011 16:27:38 by Hibernate Tools 3.2.1.GA

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Linhaspesquisa generated by hbm2java
 */
@Entity
@Table(name = "linhaspesquisa", schema = "monesp")
public class Linhaspesquisa implements java.io.Serializable {

    private int codigo;
    private String nome;
    private Set<Monografias> monografiases = new HashSet<Monografias>(0);

    public Linhaspesquisa() {
    }

     public Linhaspesquisa(String nome) {
        this.nome = nome;
    }

    public Linhaspesquisa(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }


    public Linhaspesquisa(int codigo, String nome, Set<Monografias> monografiases) {
        this.codigo = codigo;
        this.nome = nome;
        this.monografiases = monografiases;
    }

    @Id
    @Column(name = "codigo", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getCodigo() {
        return this.codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @Column(name = "nome", nullable = false, length = 100)
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "linhaspesquisa")
    public Set<Monografias> getMonografiases() {
        return this.monografiases;
    }

    public void setMonografiases(Set<Monografias> monografiases) {
        this.monografiases = monografiases;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Linhaspesquisa other = (Linhaspesquisa) obj;
        if (this.codigo != other.codigo) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.codigo;
        return hash;
    }
}