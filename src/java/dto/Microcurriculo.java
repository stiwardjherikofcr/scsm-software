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
@Table(name = "microcurriculo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Microcurriculo.findAll", query = "SELECT m FROM Microcurriculo m")
    , @NamedQuery(name = "Microcurriculo.findById", query = "SELECT m FROM Microcurriculo m WHERE m.microcurriculoPK.id = :id")
    , @NamedQuery(name = "Microcurriculo.findByMateriaCodigoMateria", query = "SELECT m FROM Microcurriculo m WHERE m.microcurriculoPK.materiaCodigoMateria = :materiaCodigoMateria")
    , @NamedQuery(name = "Microcurriculo.findByMateriaPensumCodigo", query = "SELECT m FROM Microcurriculo m WHERE m.microcurriculoPK.materiaPensumCodigo = :materiaPensumCodigo")})
public class Microcurriculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MicrocurriculoPK microcurriculoPK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "microcurriculo")
    private List<SeccionMicrocurriculo> seccionMicrocurriculoList;
    @JoinColumn(name = "area_de_formacion_id", referencedColumnName = "id")
    @ManyToOne
    private AreaFormacion areaDeFormacionId;
    @JoinColumns({
        @JoinColumn(name = "materia_codigo_materia", referencedColumnName = "codigo_materia", insertable = false, updatable = false)
        , @JoinColumn(name = "materia_pensum_codigo", referencedColumnName = "pensum_codigo", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Materia materia;

    public Microcurriculo() {
    }

    public Microcurriculo(MicrocurriculoPK microcurriculoPK) {
        this.microcurriculoPK = microcurriculoPK;
    }

    public Microcurriculo(int id, int materiaCodigoMateria, int materiaPensumCodigo) {
        this.microcurriculoPK = new MicrocurriculoPK(id, materiaCodigoMateria, materiaPensumCodigo);
    }

    public MicrocurriculoPK getMicrocurriculoPK() {
        return microcurriculoPK;
    }

    public void setMicrocurriculoPK(MicrocurriculoPK microcurriculoPK) {
        this.microcurriculoPK = microcurriculoPK;
    }

    @XmlTransient
    public List<SeccionMicrocurriculo> getSeccionMicrocurriculoList() {
        return seccionMicrocurriculoList;
    }

    public void setSeccionMicrocurriculoList(List<SeccionMicrocurriculo> seccionMicrocurriculoList) {
        this.seccionMicrocurriculoList = seccionMicrocurriculoList;
    }

    public AreaFormacion getAreaDeFormacionId() {
        return areaDeFormacionId;
    }

    public void setAreaDeFormacionId(AreaFormacion areaDeFormacionId) {
        this.areaDeFormacionId = areaDeFormacionId;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (microcurriculoPK != null ? microcurriculoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Microcurriculo)) {
            return false;
        }
        Microcurriculo other = (Microcurriculo) object;
        if ((this.microcurriculoPK == null && other.microcurriculoPK != null) || (this.microcurriculoPK != null && !this.microcurriculoPK.equals(other.microcurriculoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Microcurriculo[ microcurriculoPK=" + microcurriculoPK + " ]";
    }
    
}
