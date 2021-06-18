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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name = "seccion_microcurriculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SeccionMicrocurriculo.findAll", query = "SELECT s FROM SeccionMicrocurriculo s")
    , @NamedQuery(name = "SeccionMicrocurriculo.findById", query = "SELECT s FROM SeccionMicrocurriculo s WHERE s.id = :id")
    , @NamedQuery(name = "SeccionMicrocurriculo.findByEditable", query = "SELECT s FROM SeccionMicrocurriculo s WHERE s.editable = :editable")})
public class SeccionMicrocurriculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "editable")
    private short editable;
    @JoinColumns({
        @JoinColumn(name = "microcurriculo_id", referencedColumnName = "id")
        , @JoinColumn(name = "microcurriculo_materia_pensum_codigo", referencedColumnName = "materia_pensum_codigo")
        , @JoinColumn(name = "microcurriculo_materia_codigo_materia", referencedColumnName = "materia_codigo_materia")})
    @ManyToOne(optional = false)
    private Microcurriculo microcurriculo;
    @JoinColumn(name = "seccion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Seccion seccionId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionMicrocurriculoId")
    private List<Contenido> contenidoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionMicrocurriculoIdNuevo")
    private List<SeccionCambio> seccionCambioList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionMicrocurriculoIdAntigua")
    private List<SeccionCambio> seccionCambioList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "seccionMicrocurriculo")
    private List<TablaMicrocurriculo> tablaMicrocurriculoList;

    public SeccionMicrocurriculo() {
    }

    public SeccionMicrocurriculo(Integer id) {
        this.id = id;
    }

    public SeccionMicrocurriculo(Integer id, short editable) {
        this.id = id;
        this.editable = editable;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getEditable() {
        return editable;
    }

    public void setEditable(short editable) {
        this.editable = editable;
    }

    public Microcurriculo getMicrocurriculo() {
        return microcurriculo;
    }

    public void setMicrocurriculo(Microcurriculo microcurriculo) {
        this.microcurriculo = microcurriculo;
    }

    public Seccion getSeccionId() {
        return seccionId;
    }

    public void setSeccionId(Seccion seccionId) {
        this.seccionId = seccionId;
    }

    @XmlTransient
    public List<Contenido> getContenidoList() {
        return contenidoList;
    }

    public void setContenidoList(List<Contenido> contenidoList) {
        this.contenidoList = contenidoList;
    }

    @XmlTransient
    public List<SeccionCambio> getSeccionCambioList() {
        return seccionCambioList;
    }

    public void setSeccionCambioList(List<SeccionCambio> seccionCambioList) {
        this.seccionCambioList = seccionCambioList;
    }

    @XmlTransient
    public List<SeccionCambio> getSeccionCambioList1() {
        return seccionCambioList1;
    }

    public void setSeccionCambioList1(List<SeccionCambio> seccionCambioList1) {
        this.seccionCambioList1 = seccionCambioList1;
    }

    @XmlTransient
    public List<TablaMicrocurriculo> getTablaMicrocurriculoList() {
        return tablaMicrocurriculoList;
    }

    public void setTablaMicrocurriculoList(List<TablaMicrocurriculo> tablaMicrocurriculoList) {
        this.tablaMicrocurriculoList = tablaMicrocurriculoList;
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
        if (!(object instanceof SeccionMicrocurriculo)) {
            return false;
        }
        SeccionMicrocurriculo other = (SeccionMicrocurriculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.SeccionMicrocurriculo[ id=" + id + " ]";
    }
    
}
