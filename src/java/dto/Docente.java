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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "docente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Docente.findAll", query = "SELECT d FROM Docente d")
    , @NamedQuery(name = "Docente.findByCodigoDocente", query = "SELECT d FROM Docente d WHERE d.codigoDocente = :codigoDocente")
    , @NamedQuery(name = "Docente.findByNombre", query = "SELECT d FROM Docente d WHERE d.nombre = :nombre")
    , @NamedQuery(name = "Docente.findByApellido", query = "SELECT d FROM Docente d WHERE d.apellido = :apellido")
    , @NamedQuery(name = "Docente.findByEstado", query = "SELECT d FROM Docente d WHERE d.estado = :estado")})
public class Docente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo_docente")
    private Integer codigoDocente;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "apellido")
    private String apellido;
    @Basic(optional = false)
    @Column(name = "estado")
    private short estado;
    @Lob
    @Column(name = "img_perfil")
    private byte[] imgPerfil;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "directorPrograma")
    private List<Programa> programaList;
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamento departamentoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docente")
    private List<MateriaPeriodoGrupo> materiaPeriodoGrupoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "docente")
    private List<Usuario> usuarioList;

    public Docente() {
    }

    public Docente(Integer codigoDocente) {
        this.codigoDocente = codigoDocente;
    }

    public Docente(Integer codigoDocente, String nombre, String apellido, short estado) {
        this.codigoDocente = codigoDocente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.estado = estado;
    }

    public Integer getCodigoDocente() {
        return codigoDocente;
    }

    public void setCodigoDocente(Integer codigoDocente) {
        this.codigoDocente = codigoDocente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public short getEstado() {
        return estado;
    }

    public void setEstado(short estado) {
        this.estado = estado;
    }

    public byte[] getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(byte[] imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    @XmlTransient
    public List<Programa> getProgramaList() {
        return programaList;
    }

    public void setProgramaList(List<Programa> programaList) {
        this.programaList = programaList;
    }

    public Departamento getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Departamento departamentoId) {
        this.departamentoId = departamentoId;
    }

    @XmlTransient
    public List<MateriaPeriodoGrupo> getMateriaPeriodoGrupoList() {
        return materiaPeriodoGrupoList;
    }

    public void setMateriaPeriodoGrupoList(List<MateriaPeriodoGrupo> materiaPeriodoGrupoList) {
        this.materiaPeriodoGrupoList = materiaPeriodoGrupoList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigoDocente != null ? codigoDocente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.codigoDocente == null && other.codigoDocente != null) || (this.codigoDocente != null && !this.codigoDocente.equals(other.codigoDocente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Docente[ codigoDocente=" + codigoDocente + " ]";
    }
    
}
