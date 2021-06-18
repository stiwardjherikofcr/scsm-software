/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "materia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Materia.findAll", query = "SELECT m FROM Materia m")
    , @NamedQuery(name = "Materia.findByCodigoMateria", query = "SELECT m FROM Materia m WHERE m.materiaPK.codigoMateria = :codigoMateria")
    , @NamedQuery(name = "Materia.findByNombre", query = "SELECT m FROM Materia m WHERE m.nombre = :nombre")
    , @NamedQuery(name = "Materia.findByCreditos", query = "SELECT m FROM Materia m WHERE m.creditos = :creditos")
    , @NamedQuery(name = "Materia.findBySemestre", query = "SELECT m FROM Materia m WHERE m.semestre = :semestre")
    , @NamedQuery(name = "Materia.findByPensumCodigo", query = "SELECT m FROM Materia m WHERE m.materiaPK.pensumCodigo = :pensumCodigo")
    , @NamedQuery(name = "Materia.findByHt", query = "SELECT m FROM Materia m WHERE m.ht = :ht")
    , @NamedQuery(name = "Materia.findByHp", query = "SELECT m FROM Materia m WHERE m.hp = :hp")
    , @NamedQuery(name = "Materia.findByHti", query = "SELECT m FROM Materia m WHERE m.hti = :hti")
    , @NamedQuery(name = "Materia.findByCr", query = "SELECT m FROM Materia m WHERE m.cr = :cr")})
public class Materia implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MateriaPK materiaPK;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "creditos")
    private int creditos;
    @Basic(optional = false)
    @Column(name = "semestre")
    private int semestre;
    @Basic(optional = false)
    @Column(name = "ht")
    private int ht;
    @Basic(optional = false)
    @Column(name = "hp")
    private int hp;
    @Basic(optional = false)
    @Column(name = "hti")
    private int hti;
    @Column(name = "cr")
    private Integer cr;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materia")
    private List<PrerrequisitoMateria> prerrequisitoMateriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materia1")
    private List<PrerrequisitoMateria> prerrequisitoMateriaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materia")
    private List<EquivalenciaMateria> equivalenciaMateriaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materia")
    private List<Microcurriculo> microcurriculoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "materia")
    private List<MateriaPeriodo> materiaPeriodoList;
    @JoinColumns({
        @JoinColumn(name = "pensum_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
        , @JoinColumn(name = "pensum_programa_codigo", referencedColumnName = "programa_codigo")})
    @ManyToOne(optional = false)
    private Pensum pensum;
    @JoinColumn(name = "tipo_asignatura_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoAsignatura tipoAsignaturaId;

    public Materia() {
    }

    public Materia(MateriaPK materiaPK) {
        this.materiaPK = materiaPK;
    }

    public Materia(MateriaPK materiaPK, String nombre, int creditos, int semestre, int ht, int hp, int hti) {
        this.materiaPK = materiaPK;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
        this.ht = ht;
        this.hp = hp;
        this.hti = hti;
    }

    public Materia(int codigoMateria, int pensumCodigo) {
        this.materiaPK = new MateriaPK(codigoMateria, pensumCodigo);
    }

    public MateriaPK getMateriaPK() {
        return materiaPK;
    }

    public void setMateriaPK(MateriaPK materiaPK) {
        this.materiaPK = materiaPK;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getHt() {
        return ht;
    }

    public void setHt(int ht) {
        this.ht = ht;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHti() {
        return hti;
    }

    public void setHti(int hti) {
        this.hti = hti;
    }

    public Integer getCr() {
        return cr;
    }

    public void setCr(Integer cr) {
        this.cr = cr;
    }

    @XmlTransient
    public List<PrerrequisitoMateria> getPrerrequisitoMateriaList() {
        return prerrequisitoMateriaList;
    }

    public void setPrerrequisitoMateriaList(List<PrerrequisitoMateria> prerrequisitoMateriaList) {
        this.prerrequisitoMateriaList = prerrequisitoMateriaList;
    }

    @XmlTransient
    public List<PrerrequisitoMateria> getPrerrequisitoMateriaList1() {
        return prerrequisitoMateriaList1;
    }

    public void setPrerrequisitoMateriaList1(List<PrerrequisitoMateria> prerrequisitoMateriaList1) {
        this.prerrequisitoMateriaList1 = prerrequisitoMateriaList1;
    }

    @XmlTransient
    public List<EquivalenciaMateria> getEquivalenciaMateriaList() {
        return equivalenciaMateriaList;
    }

    public void setEquivalenciaMateriaList(List<EquivalenciaMateria> equivalenciaMateriaList) {
        this.equivalenciaMateriaList = equivalenciaMateriaList;
    }

    @XmlTransient
    public List<Microcurriculo> getMicrocurriculoList() {
        return microcurriculoList;
    }

    public void setMicrocurriculoList(List<Microcurriculo> microcurriculoList) {
        this.microcurriculoList = microcurriculoList;
    }

    @XmlTransient
    public List<MateriaPeriodo> getMateriaPeriodoList() {
        return materiaPeriodoList;
    }

    public void setMateriaPeriodoList(List<MateriaPeriodo> materiaPeriodoList) {
        this.materiaPeriodoList = materiaPeriodoList;
    }

    public Pensum getPensum() {
        return pensum;
    }

    public void setPensum(Pensum pensum) {
        this.pensum = pensum;
    }

    public TipoAsignatura getTipoAsignaturaId() {
        return tipoAsignaturaId;
    }

    public void setTipoAsignaturaId(TipoAsignatura tipoAsignaturaId) {
        this.tipoAsignaturaId = tipoAsignaturaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (materiaPK != null ? materiaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Materia)) {
            return false;
        }
        Materia other = (Materia) object;
        if ((this.materiaPK == null && other.materiaPK != null) || (this.materiaPK != null && !this.materiaPK.equals(other.materiaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Materia[ materiaPK=" + materiaPK + " ]";
    }
    
}
