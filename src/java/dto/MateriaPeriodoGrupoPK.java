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
public class MateriaPeriodoGrupoPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "grupo")
    private String grupo;
    @Basic(optional = false)
    @Column(name = "docente_codigo")
    private int docenteCodigo;
    @Basic(optional = false)
    @Column(name = "materia_periodo_anio")
    private int materiaPeriodoAnio;
    @Basic(optional = false)
    @Column(name = "materia_periodo_semestre_anio")
    private int materiaPeriodoSemestreAnio;
    @Basic(optional = false)
    @Column(name = "materia_periodo_materia_pensum_codigo")
    private int materiaPeriodoMateriaPensumCodigo;
    @Basic(optional = false)
    @Column(name = "materia_periodo_materia_codigo_materia")
    private int materiaPeriodoMateriaCodigoMateria;

    public MateriaPeriodoGrupoPK() {
    }

    public MateriaPeriodoGrupoPK(String grupo, int docenteCodigo, int materiaPeriodoAnio, int materiaPeriodoSemestreAnio, int materiaPeriodoMateriaPensumCodigo, int materiaPeriodoMateriaCodigoMateria) {
        this.grupo = grupo;
        this.docenteCodigo = docenteCodigo;
        this.materiaPeriodoAnio = materiaPeriodoAnio;
        this.materiaPeriodoSemestreAnio = materiaPeriodoSemestreAnio;
        this.materiaPeriodoMateriaPensumCodigo = materiaPeriodoMateriaPensumCodigo;
        this.materiaPeriodoMateriaCodigoMateria = materiaPeriodoMateriaCodigoMateria;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getDocenteCodigo() {
        return docenteCodigo;
    }

    public void setDocenteCodigo(int docenteCodigo) {
        this.docenteCodigo = docenteCodigo;
    }

    public int getMateriaPeriodoAnio() {
        return materiaPeriodoAnio;
    }

    public void setMateriaPeriodoAnio(int materiaPeriodoAnio) {
        this.materiaPeriodoAnio = materiaPeriodoAnio;
    }

    public int getMateriaPeriodoSemestreAnio() {
        return materiaPeriodoSemestreAnio;
    }

    public void setMateriaPeriodoSemestreAnio(int materiaPeriodoSemestreAnio) {
        this.materiaPeriodoSemestreAnio = materiaPeriodoSemestreAnio;
    }

    public int getMateriaPeriodoMateriaPensumCodigo() {
        return materiaPeriodoMateriaPensumCodigo;
    }

    public void setMateriaPeriodoMateriaPensumCodigo(int materiaPeriodoMateriaPensumCodigo) {
        this.materiaPeriodoMateriaPensumCodigo = materiaPeriodoMateriaPensumCodigo;
    }

    public int getMateriaPeriodoMateriaCodigoMateria() {
        return materiaPeriodoMateriaCodigoMateria;
    }

    public void setMateriaPeriodoMateriaCodigoMateria(int materiaPeriodoMateriaCodigoMateria) {
        this.materiaPeriodoMateriaCodigoMateria = materiaPeriodoMateriaCodigoMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grupo != null ? grupo.hashCode() : 0);
        hash += (int) docenteCodigo;
        hash += (int) materiaPeriodoAnio;
        hash += (int) materiaPeriodoSemestreAnio;
        hash += (int) materiaPeriodoMateriaPensumCodigo;
        hash += (int) materiaPeriodoMateriaCodigoMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaPeriodoGrupoPK)) {
            return false;
        }
        MateriaPeriodoGrupoPK other = (MateriaPeriodoGrupoPK) object;
        if ((this.grupo == null && other.grupo != null) || (this.grupo != null && !this.grupo.equals(other.grupo))) {
            return false;
        }
        if (this.docenteCodigo != other.docenteCodigo) {
            return false;
        }
        if (this.materiaPeriodoAnio != other.materiaPeriodoAnio) {
            return false;
        }
        if (this.materiaPeriodoSemestreAnio != other.materiaPeriodoSemestreAnio) {
            return false;
        }
        if (this.materiaPeriodoMateriaPensumCodigo != other.materiaPeriodoMateriaPensumCodigo) {
            return false;
        }
        if (this.materiaPeriodoMateriaCodigoMateria != other.materiaPeriodoMateriaCodigoMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.MateriaPeriodoGrupoPK[ grupo=" + grupo + ", docenteCodigo=" + docenteCodigo + ", materiaPeriodoAnio=" + materiaPeriodoAnio + ", materiaPeriodoSemestreAnio=" + materiaPeriodoSemestreAnio + ", materiaPeriodoMateriaPensumCodigo=" + materiaPeriodoMateriaPensumCodigo + ", materiaPeriodoMateriaCodigoMateria=" + materiaPeriodoMateriaCodigoMateria + " ]";
    }
    
}
