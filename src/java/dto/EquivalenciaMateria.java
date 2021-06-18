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
@Table(name = "equivalencia_materia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EquivalenciaMateria.findAll", query = "SELECT e FROM EquivalenciaMateria e")
    , @NamedQuery(name = "EquivalenciaMateria.findByEquivalenciaMateria", query = "SELECT e FROM EquivalenciaMateria e WHERE e.equivalenciaMateria = :equivalenciaMateria")
    , @NamedQuery(name = "EquivalenciaMateria.findByNombre", query = "SELECT e FROM EquivalenciaMateria e WHERE e.nombre = :nombre")
    , @NamedQuery(name = "EquivalenciaMateria.findById", query = "SELECT e FROM EquivalenciaMateria e WHERE e.id = :id")})
public class EquivalenciaMateria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "equivalencia_materia")
    private int equivalenciaMateria;
    @Column(name = "nombre")
    private String nombre;
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

    public EquivalenciaMateria() {
    }

    public EquivalenciaMateria(Integer id) {
        this.id = id;
    }

    public EquivalenciaMateria(Integer id, int equivalenciaMateria) {
        this.id = id;
        this.equivalenciaMateria = equivalenciaMateria;
    }

    public int getEquivalenciaMateria() {
        return equivalenciaMateria;
    }

    public void setEquivalenciaMateria(int equivalenciaMateria) {
        this.equivalenciaMateria = equivalenciaMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquivalenciaMateria)) {
            return false;
        }
        EquivalenciaMateria other = (EquivalenciaMateria) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.EquivalenciaMateria[ id=" + id + " ]";
    }
    
}
