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
public class MicrocurriculoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private int id;
    @Basic(optional = false)
    @Column(name = "materia_codigo_materia")
    private int materiaCodigoMateria;
    @Basic(optional = false)
    @Column(name = "materia_pensum_codigo")
    private int materiaPensumCodigo;

    public MicrocurriculoPK() {
    }

    public MicrocurriculoPK(int id, int materiaCodigoMateria, int materiaPensumCodigo) {
        this.id = id;
        this.materiaCodigoMateria = materiaCodigoMateria;
        this.materiaPensumCodigo = materiaPensumCodigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMateriaCodigoMateria() {
        return materiaCodigoMateria;
    }

    public void setMateriaCodigoMateria(int materiaCodigoMateria) {
        this.materiaCodigoMateria = materiaCodigoMateria;
    }

    public int getMateriaPensumCodigo() {
        return materiaPensumCodigo;
    }

    public void setMateriaPensumCodigo(int materiaPensumCodigo) {
        this.materiaPensumCodigo = materiaPensumCodigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) materiaCodigoMateria;
        hash += (int) materiaPensumCodigo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MicrocurriculoPK)) {
            return false;
        }
        MicrocurriculoPK other = (MicrocurriculoPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.materiaCodigoMateria != other.materiaCodigoMateria) {
            return false;
        }
        if (this.materiaPensumCodigo != other.materiaPensumCodigo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.MicrocurriculoPK[ id=" + id + ", materiaCodigoMateria=" + materiaCodigoMateria + ", materiaPensumCodigo=" + materiaPensumCodigo + " ]";
    }
    
}
