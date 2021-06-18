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
public class TablaMicrocurriculoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "seccion_microcurriculo_id")
    private int seccionMicrocurriculoId;

    public TablaMicrocurriculoPK() {
    }

    public TablaMicrocurriculoPK(int id, int seccionMicrocurriculoId) {
        this.id = id;
        this.seccionMicrocurriculoId = seccionMicrocurriculoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeccionMicrocurriculoId() {
        return seccionMicrocurriculoId;
    }

    public void setSeccionMicrocurriculoId(int seccionMicrocurriculoId) {
        this.seccionMicrocurriculoId = seccionMicrocurriculoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) seccionMicrocurriculoId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TablaMicrocurriculoPK)) {
            return false;
        }
        TablaMicrocurriculoPK other = (TablaMicrocurriculoPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.seccionMicrocurriculoId != other.seccionMicrocurriculoId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.TablaMicrocurriculoPK[ id=" + id + ", seccionMicrocurriculoId=" + seccionMicrocurriculoId + " ]";
    }
    
}
