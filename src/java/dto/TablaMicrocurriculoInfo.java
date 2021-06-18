/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "tabla_microcurriculo_info")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TablaMicrocurriculoInfo.findAll", query = "SELECT t FROM TablaMicrocurriculoInfo t")
    , @NamedQuery(name = "TablaMicrocurriculoInfo.findByIdFila", query = "SELECT t FROM TablaMicrocurriculoInfo t WHERE t.tablaMicrocurriculoInfoPK.idFila = :idFila")
    , @NamedQuery(name = "TablaMicrocurriculoInfo.findByIdColumna", query = "SELECT t FROM TablaMicrocurriculoInfo t WHERE t.tablaMicrocurriculoInfoPK.idColumna = :idColumna")
    , @NamedQuery(name = "TablaMicrocurriculoInfo.findByTablaMicrocurriculoId", query = "SELECT t FROM TablaMicrocurriculoInfo t WHERE t.tablaMicrocurriculoInfoPK.tablaMicrocurriculoId = :tablaMicrocurriculoId")
    , @NamedQuery(name = "TablaMicrocurriculoInfo.findByTablaMicrocurriculoSeccionMicrocurriculoId", query = "SELECT t FROM TablaMicrocurriculoInfo t WHERE t.tablaMicrocurriculoInfoPK.tablaMicrocurriculoSeccionMicrocurriculoId = :tablaMicrocurriculoSeccionMicrocurriculoId")})
public class TablaMicrocurriculoInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TablaMicrocurriculoInfoPK tablaMicrocurriculoInfoPK;
    @JoinColumn(name = "contenido_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Contenido contenidoId;
    @JoinColumns({
        @JoinColumn(name = "tabla_microcurriculo_id", referencedColumnName = "id", insertable = false, updatable = false)
        , @JoinColumn(name = "tabla_microcurriculo_seccion_microcurriculo_id", referencedColumnName = "seccion_microcurriculo_id", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private TablaMicrocurriculo tablaMicrocurriculo;

    public TablaMicrocurriculoInfo() {
    }

    public TablaMicrocurriculoInfo(TablaMicrocurriculoInfoPK tablaMicrocurriculoInfoPK) {
        this.tablaMicrocurriculoInfoPK = tablaMicrocurriculoInfoPK;
    }

    public TablaMicrocurriculoInfo(int idFila, int idColumna, int tablaMicrocurriculoId, int tablaMicrocurriculoSeccionMicrocurriculoId) {
        this.tablaMicrocurriculoInfoPK = new TablaMicrocurriculoInfoPK(idFila, idColumna, tablaMicrocurriculoId, tablaMicrocurriculoSeccionMicrocurriculoId);
    }

    public TablaMicrocurriculoInfoPK getTablaMicrocurriculoInfoPK() {
        return tablaMicrocurriculoInfoPK;
    }

    public void setTablaMicrocurriculoInfoPK(TablaMicrocurriculoInfoPK tablaMicrocurriculoInfoPK) {
        this.tablaMicrocurriculoInfoPK = tablaMicrocurriculoInfoPK;
    }

    public Contenido getContenidoId() {
        return contenidoId;
    }

    public void setContenidoId(Contenido contenidoId) {
        this.contenidoId = contenidoId;
    }

    public TablaMicrocurriculo getTablaMicrocurriculo() {
        return tablaMicrocurriculo;
    }

    public void setTablaMicrocurriculo(TablaMicrocurriculo tablaMicrocurriculo) {
        this.tablaMicrocurriculo = tablaMicrocurriculo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tablaMicrocurriculoInfoPK != null ? tablaMicrocurriculoInfoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TablaMicrocurriculoInfo)) {
            return false;
        }
        TablaMicrocurriculoInfo other = (TablaMicrocurriculoInfo) object;
        if ((this.tablaMicrocurriculoInfoPK == null && other.tablaMicrocurriculoInfoPK != null) || (this.tablaMicrocurriculoInfoPK != null && !this.tablaMicrocurriculoInfoPK.equals(other.tablaMicrocurriculoInfoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.TablaMicrocurriculoInfo[ tablaMicrocurriculoInfoPK=" + tablaMicrocurriculoInfoPK + " ]";
    }
    
}
