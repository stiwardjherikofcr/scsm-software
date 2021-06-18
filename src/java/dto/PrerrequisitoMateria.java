/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Manuel
 */
@Entity
@Table(name = "prerrequisito_materia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PrerrequisitoMateria.findAll", query = "SELECT p FROM PrerrequisitoMateria p")
    , @NamedQuery(name = "PrerrequisitoMateria.findById", query = "SELECT p FROM PrerrequisitoMateria p WHERE p.id = :id")})
public class PrerrequisitoMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumns({
        @JoinColumn(name = "materia_codigo_materia", referencedColumnName = "codigo_materia")
        , @JoinColumn(name = "materia_pensum_codigo", referencedColumnName = "pensum_codigo")})
    @ManyToOne(optional = false)
    private Materia materia;
    @JoinColumns({
        @JoinColumn(name = "materia_codigo_prerrequisito", referencedColumnName = "codigo_materia")
        , @JoinColumn(name = "materia_pensum_prerrequisito", referencedColumnName = "pensum_codigo")})
    @ManyToOne(optional = false)
    private Materia materia1;

    public PrerrequisitoMateria() {
    }

    public PrerrequisitoMateria(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Materia getMateria1() {
        return materia1;
    }

    public void setMateria1(Materia materia1) {
        this.materia1 = materia1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrerrequisitoMateria)) {
            return false;
        }
        PrerrequisitoMateria other = (PrerrequisitoMateria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PrerrequisitoMateria[ id=" + id + " ]";
    }
    
}
