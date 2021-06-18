/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Manuel
 */
@Embeddable
public class TablaMicrocurriculoInfoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id_fila")
    private int idFila;
    @Basic(optional = false)
    @Column(name = "id_columna")
    private int idColumna;
    @Basic(optional = false)
    @Column(name = "tabla_microcurriculo_id")
    private int tablaMicrocurriculoId;
    @Basic(optional = false)
    @Column(name = "tabla_microcurriculo_seccion_microcurriculo_id")
    private int tablaMicrocurriculoSeccionMicrocurriculoId;

    public TablaMicrocurriculoInfoPK() {
    }

    public TablaMicrocurriculoInfoPK(int idFila, int idColumna, int tablaMicrocurriculoId, int tablaMicrocurriculoSeccionMicrocurriculoId) {
        this.idFila = idFila;
        this.idColumna = idColumna;
        this.tablaMicrocurriculoId = tablaMicrocurriculoId;
        this.tablaMicrocurriculoSeccionMicrocurriculoId = tablaMicrocurriculoSeccionMicrocurriculoId;
    }

    public int getIdFila() {
        return idFila;
    }

    public void setIdFila(int idFila) {
        this.idFila = idFila;
    }

    public int getIdColumna() {
        return idColumna;
    }

    public void setIdColumna(int idColumna) {
        this.idColumna = idColumna;
    }

    public int getTablaMicrocurriculoId() {
        return tablaMicrocurriculoId;
    }

    public void setTablaMicrocurriculoId(int tablaMicrocurriculoId) {
        this.tablaMicrocurriculoId = tablaMicrocurriculoId;
    }

    public int getTablaMicrocurriculoSeccionMicrocurriculoId() {
        return tablaMicrocurriculoSeccionMicrocurriculoId;
    }

    public void setTablaMicrocurriculoSeccionMicrocurriculoId(int tablaMicrocurriculoSeccionMicrocurriculoId) {
        this.tablaMicrocurriculoSeccionMicrocurriculoId = tablaMicrocurriculoSeccionMicrocurriculoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idFila;
        hash += (int) idColumna;
        hash += (int) tablaMicrocurriculoId;
        hash += (int) tablaMicrocurriculoSeccionMicrocurriculoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TablaMicrocurriculoInfoPK)) {
            return false;
        }
        TablaMicrocurriculoInfoPK other = (TablaMicrocurriculoInfoPK) object;
        if (this.idFila != other.idFila) {
            return false;
        }
        if (this.idColumna != other.idColumna) {
            return false;
        }
        if (this.tablaMicrocurriculoId != other.tablaMicrocurriculoId) {
            return false;
        }
        if (this.tablaMicrocurriculoSeccionMicrocurriculoId != other.tablaMicrocurriculoSeccionMicrocurriculoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.TablaMicrocurriculoInfoPK[ idFila=" + idFila + ", idColumna=" + idColumna + ", tablaMicrocurriculoId=" + tablaMicrocurriculoId + ", tablaMicrocurriculoSeccionMicrocurriculoId=" + tablaMicrocurriculoSeccionMicrocurriculoId + " ]";
    }
    
}
