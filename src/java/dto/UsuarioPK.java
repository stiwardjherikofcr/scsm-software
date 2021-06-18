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
public class UsuarioPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "rol_id")
    private int rolId;
    @Basic(optional = false)
    @Column(name = "docente_codigo")
    private int docenteCodigo;

    public UsuarioPK() {
    }

    public UsuarioPK(int rolId, int docenteCodigo) {
        this.rolId = rolId;
        this.docenteCodigo = docenteCodigo;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public int getDocenteCodigo() {
        return docenteCodigo;
    }

    public void setDocenteCodigo(int docenteCodigo) {
        this.docenteCodigo = docenteCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) rolId;
        hash += (int) docenteCodigo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioPK)) {
            return false;
        }
        UsuarioPK other = (UsuarioPK) object;
        if (this.rolId != other.rolId) {
            return false;
        }
        if (this.docenteCodigo != other.docenteCodigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.UsuarioPK[ rolId=" + rolId + ", docenteCodigo=" + docenteCodigo + " ]";
    }
    
}
