/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Manuel
 */
@Entity
@Table(name = "pensum")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pensum.findAll", query = "SELECT p FROM Pensum p")
    , @NamedQuery(name = "Pensum.findByCodigo", query = "SELECT p FROM Pensum p WHERE p.pensumPK.codigo = :codigo")
    , @NamedQuery(name = "Pensum.findByFechaInicioVigencia", query = "SELECT p FROM Pensum p WHERE p.fechaInicioVigencia = :fechaInicioVigencia")
    , @NamedQuery(name = "Pensum.findByProgramaCodigo", query = "SELECT p FROM Pensum p WHERE p.pensumPK.programaCodigo = :programaCodigo")
    , @NamedQuery(name = "Pensum.findByFechaFinVigencia", query = "SELECT p FROM Pensum p WHERE p.fechaFinVigencia = :fechaFinVigencia")
    , @NamedQuery(name = "Pensum.findByEstadoVisualizacion", query = "SELECT p FROM Pensum p WHERE p.estadoVisualizacion = :estadoVisualizacion")})
public class Pensum implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PensumPK pensumPK;
    @Column(name = "fecha_inicio_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaInicioVigencia;
    @Column(name = "fecha_fin_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaFinVigencia;
    @Basic(optional = false)
    @Column(name = "estado_visualizacion")
    private short estadoVisualizacion;
    @JoinColumn(name = "programa_codigo", referencedColumnName = "codigo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Programa programa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pensum")
    private List<Materia> materiaList;

    public Pensum() {
    }

    public Pensum(PensumPK pensumPK) {
        this.pensumPK = pensumPK;
    }

    public Pensum(PensumPK pensumPK, short estadoVisualizacion) {
        this.pensumPK = pensumPK;
        this.estadoVisualizacion = estadoVisualizacion;
    }

    public Pensum(int codigo, int programaCodigo) {
        this.pensumPK = new PensumPK(codigo, programaCodigo);
    }

    public PensumPK getPensumPK() {
        return pensumPK;
    }

    public void setPensumPK(PensumPK pensumPK) {
        this.pensumPK = pensumPK;
    }

    public Date getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public void setFechaInicioVigencia(Date fechaInicioVigencia) {
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public Date getFechaFinVigencia() {
        return fechaFinVigencia;
    }

    public void setFechaFinVigencia(Date fechaFinVigencia) {
        this.fechaFinVigencia = fechaFinVigencia;
    }

    public short getEstadoVisualizacion() {
        return estadoVisualizacion;
    }

    public void setEstadoVisualizacion(short estadoVisualizacion) {
        this.estadoVisualizacion = estadoVisualizacion;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    @XmlTransient
    public List<Materia> getMateriaList() {
        return materiaList;
    }

    public void setMateriaList(List<Materia> materiaList) {
        this.materiaList = materiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pensumPK != null ? pensumPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pensum)) {
            return false;
        }
        Pensum other = (Pensum) object;
        if ((this.pensumPK == null && other.pensumPK != null) || (this.pensumPK != null && !this.pensumPK.equals(other.pensumPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Pensum[ pensumPK=" + pensumPK + " ]";
    }
    
}
