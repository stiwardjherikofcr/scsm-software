/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Manuel
 */
@Entity
@Table(name = "area_formacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaFormacion.findAll", query = "SELECT a FROM AreaFormacion a")
    , @NamedQuery(name = "AreaFormacion.findById", query = "SELECT a FROM AreaFormacion a WHERE a.id = :id")
    , @NamedQuery(name = "AreaFormacion.findByNombre", query = "SELECT a FROM AreaFormacion a WHERE a.nombre = :nombre")})
public class AreaFormacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "areaDeFormacionId")
    private List<Microcurriculo> microcurriculoList;

    public AreaFormacion() {
    }

    public AreaFormacion(Integer id) {
        this.id = id;
    }

    public AreaFormacion(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public List<Microcurriculo> getMicrocurriculoList() {
        return microcurriculoList;
    }

    public void setMicrocurriculoList(List<Microcurriculo> microcurriculoList) {
        this.microcurriculoList = microcurriculoList;
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
        if (!(object instanceof AreaFormacion)) {
            return false;
        }
        AreaFormacion other = (AreaFormacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.AreaFormacion[ id=" + id + " ]";
    }
    
}
