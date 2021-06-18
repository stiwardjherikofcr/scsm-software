/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "materia_periodo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MateriaPeriodo.findAll", query = "SELECT m FROM MateriaPeriodo m")
    , @NamedQuery(name = "MateriaPeriodo.findByAnio", query = "SELECT m FROM MateriaPeriodo m WHERE m.materiaPeriodoPK.anio = :anio")
    , @NamedQuery(name = "MateriaPeriodo.findBySemestreAnio", query = "SELECT m FROM MateriaPeriodo m WHERE m.materiaPeriodoPK.semestreAnio = :semestreAnio")
    , @NamedQuery(name = "MateriaPeriodo.findByMateriaCodigoMateria", query = "SELECT m FROM MateriaPeriodo m WHERE m.materiaPeriodoPK.materiaCodigoMateria = :materiaCodigoMateria")
    , @NamedQuery(name = "MateriaPeriodo.findByMateriaPensumCodigo", query = "SELECT m FROM MateriaPeriodo m WHERE m.materiaPeriodoPK.materiaPensumCodigo = :materiaPensumCodigo")})
public class MateriaPeriodo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MateriaPeriodoPK materiaPeriodoPK;
    @JoinColumns({
        @JoinColumn(name = "materia_codigo_materia", referencedColumnName = "codigo_materia", insertable = false, updatable = false)
        , @JoinColumn(name = "materia_pensum_codigo", referencedColumnName = "pensum_codigo", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Materia materia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materiaPeriodo")
    private List<MateriaPeriodoGrupo> materiaPeriodoGrupoList;

    public MateriaPeriodo() {
    }

    public MateriaPeriodo(MateriaPeriodoPK materiaPeriodoPK) {
        this.materiaPeriodoPK = materiaPeriodoPK;
    }

    public MateriaPeriodo(int anio, int semestreAnio, int materiaCodigoMateria, int materiaPensumCodigo) {
        this.materiaPeriodoPK = new MateriaPeriodoPK(anio, semestreAnio, materiaCodigoMateria, materiaPensumCodigo);
    }

    public MateriaPeriodoPK getMateriaPeriodoPK() {
        return materiaPeriodoPK;
    }

    public void setMateriaPeriodoPK(MateriaPeriodoPK materiaPeriodoPK) {
        this.materiaPeriodoPK = materiaPeriodoPK;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    @XmlTransient
    public List<MateriaPeriodoGrupo> getMateriaPeriodoGrupoList() {
        return materiaPeriodoGrupoList;
    }

    public void setMateriaPeriodoGrupoList(List<MateriaPeriodoGrupo> materiaPeriodoGrupoList) {
        this.materiaPeriodoGrupoList = materiaPeriodoGrupoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (materiaPeriodoPK != null ? materiaPeriodoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MateriaPeriodo)) {
            return false;
        }
        MateriaPeriodo other = (MateriaPeriodo) object;
        if ((this.materiaPeriodoPK == null && other.materiaPeriodoPK != null) || (this.materiaPeriodoPK != null && !this.materiaPeriodoPK.equals(other.materiaPeriodoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.MateriaPeriodo[ materiaPeriodoPK=" + materiaPeriodoPK + " ]";
    }
    
}
