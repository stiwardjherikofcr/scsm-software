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
public class PensumPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "codigo")
    private int codigo;
    @Basic(optional = false)
    @Column(name = "programa_codigo")
    private int programaCodigo;

    public PensumPK() {
    }

    public PensumPK(int codigo, int programaCodigo) {
        this.codigo = codigo;
        this.programaCodigo = programaCodigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getProgramaCodigo() {
        return programaCodigo;
    }

    public void setProgramaCodigo(int programaCodigo) {
        this.programaCodigo = programaCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codigo;
        hash += (int) programaCodigo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PensumPK)) {
            return false;
        }
        PensumPK other = (PensumPK) object;
        if (this.codigo != other.codigo) {
            return false;
        }
        if (this.programaCodigo != other.programaCodigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PensumPK[ codigo=" + codigo + ", programaCodigo=" + programaCodigo + " ]";
    }
    
}
