/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "encabezado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Encabezado.findAll", query = "SELECT e FROM Encabezado e")
    , @NamedQuery(name = "Encabezado.findById", query = "SELECT e FROM Encabezado e WHERE e.id = :id")
    , @NamedQuery(name = "Encabezado.findByNombre", query = "SELECT e FROM Encabezado e WHERE e.nombre = :nombre")})
public class Encabezado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "encabezadoId")
    private List<EncabezadoTabla> encabezadoTablaList;

    public Encabezado() {
    }

    public Encabezado(Integer id) {
        this.id = id;
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
    public List<EncabezadoTabla> getEncabezadoTablaList() {
        return encabezadoTablaList;
    }

    public void setEncabezadoTablaList(List<EncabezadoTabla> encabezadoTablaList) {
        this.encabezadoTablaList = encabezadoTablaList;
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
        if (!(object instanceof Encabezado)) {
            return false;
        }
        Encabezado other = (Encabezado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Encabezado[ id=" + id + " ]";
    }
    
}
