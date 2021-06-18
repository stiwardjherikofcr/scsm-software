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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "tabla_microcurriculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TablaMicrocurriculo.findAll", query = "SELECT t FROM TablaMicrocurriculo t")
    , @NamedQuery(name = "TablaMicrocurriculo.findById", query = "SELECT t FROM TablaMicrocurriculo t WHERE t.tablaMicrocurriculoPK.id = :id")
    , @NamedQuery(name = "TablaMicrocurriculo.findByCantidadFilas", query = "SELECT t FROM TablaMicrocurriculo t WHERE t.cantidadFilas = :cantidadFilas")
    , @NamedQuery(name = "TablaMicrocurriculo.findBySeccionMicrocurriculoId", query = "SELECT t FROM TablaMicrocurriculo t WHERE t.tablaMicrocurriculoPK.seccionMicrocurriculoId = :seccionMicrocurriculoId")
    , @NamedQuery(name = "TablaMicrocurriculo.findByCantidadColumnas", query = "SELECT t FROM TablaMicrocurriculo t WHERE t.cantidadColumnas = :cantidadColumnas")})
public class TablaMicrocurriculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TablaMicrocurriculoPK tablaMicrocurriculoPK;
    @Basic(optional = false)
    @Column(name = "cantidad_filas")
    private int cantidadFilas;
    @Column(name = "cantidad_columnas")
    private Integer cantidadColumnas;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tablaMicrocurriculo")
    private List<TablaMicrocurriculoInfo> tablaMicrocurriculoInfoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tablaMicrocurriculo")
    private List<EncabezadoTabla> encabezadoTablaList;
    @JoinColumn(name = "seccion_microcurriculo_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private SeccionMicrocurriculo seccionMicrocurriculo;

    public TablaMicrocurriculo() {
    }

    public TablaMicrocurriculo(TablaMicrocurriculoPK tablaMicrocurriculoPK) {
        this.tablaMicrocurriculoPK = tablaMicrocurriculoPK;
    }

    public TablaMicrocurriculo(TablaMicrocurriculoPK tablaMicrocurriculoPK, int cantidadFilas) {
        this.tablaMicrocurriculoPK = tablaMicrocurriculoPK;
        this.cantidadFilas = cantidadFilas;
    }

    public TablaMicrocurriculo(int id, int seccionMicrocurriculoId) {
        this.tablaMicrocurriculoPK = new TablaMicrocurriculoPK(id, seccionMicrocurriculoId);
    }

    public TablaMicrocurriculoPK getTablaMicrocurriculoPK() {
        return tablaMicrocurriculoPK;
    }

    public void setTablaMicrocurriculoPK(TablaMicrocurriculoPK tablaMicrocurriculoPK) {
        this.tablaMicrocurriculoPK = tablaMicrocurriculoPK;
    }

    public int getCantidadFilas() {
        return cantidadFilas;
    }

    public void setCantidadFilas(int cantidadFilas) {
        this.cantidadFilas = cantidadFilas;
    }

    public Integer getCantidadColumnas() {
        return cantidadColumnas;
    }

    public void setCantidadColumnas(Integer cantidadColumnas) {
        this.cantidadColumnas = cantidadColumnas;
    }

    @XmlTransient
    public List<TablaMicrocurriculoInfo> getTablaMicrocurriculoInfoList() {
        return tablaMicrocurriculoInfoList;
    }

    public void setTablaMicrocurriculoInfoList(List<TablaMicrocurriculoInfo> tablaMicrocurriculoInfoList) {
        this.tablaMicrocurriculoInfoList = tablaMicrocurriculoInfoList;
    }

    @XmlTransient
    public List<EncabezadoTabla> getEncabezadoTablaList() {
        return encabezadoTablaList;
    }

    public void setEncabezadoTablaList(List<EncabezadoTabla> encabezadoTablaList) {
        this.encabezadoTablaList = encabezadoTablaList;
    }

    public SeccionMicrocurriculo getSeccionMicrocurriculo() {
        return seccionMicrocurriculo;
    }

    public void setSeccionMicrocurriculo(SeccionMicrocurriculo seccionMicrocurriculo) {
        this.seccionMicrocurriculo = seccionMicrocurriculo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tablaMicrocurriculoPK != null ? tablaMicrocurriculoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TablaMicrocurriculo)) {
            return false;
        }
        TablaMicrocurriculo other = (TablaMicrocurriculo) object;
        if ((this.tablaMicrocurriculoPK == null && other.tablaMicrocurriculoPK != null) || (this.tablaMicrocurriculoPK != null && !this.tablaMicrocurriculoPK.equals(other.tablaMicrocurriculoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.TablaMicrocurriculo[ tablaMicrocurriculoPK=" + tablaMicrocurriculoPK + " ]";
    }
    
}
