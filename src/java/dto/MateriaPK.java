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
public class MateriaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "codigo_materia")
    private int codigoMateria;
    @Basic(optional = false)
    @Column(name = "pensum_codigo")
    private int pensumCodigo;

    public MateriaPK() {
    }

    public MateriaPK(int codigoMateria, int pensumCodigo) {
        this.codigoMateria = codigoMateria;
        this.pensumCodigo = pensumCodigo;
    }

    public int getCodigoMateria() {
        return codigoMateria;
    }

    public void setCodigoMateria(int codigoMateria) {
        this.codigoMateria = codigoMateria;
    }

    public int getPensumCodigo() {
        return pensumCodigo;
    }

    public void setPensumCodigo(int pensumCodigo) {
        this.pensumCodigo = pensumCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codigoMateria;
        hash += (int) pensumCodigo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaPK)) {
            return false;
        }
        MateriaPK other = (MateriaPK) object;
        if (this.codigoMateria != other.codigoMateria) {
            return false;
        }
        if (this.pensumCodigo != other.pensumCodigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.MateriaPK[ codigoMateria=" + codigoMateria + ", pensumCodigo=" + pensumCodigo + " ]";
    }
    
}
