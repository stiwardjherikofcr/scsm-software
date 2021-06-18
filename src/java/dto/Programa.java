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
@Table(name = "programa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Programa.findAll", query = "SELECT p FROM Programa p")
    , @NamedQuery(name = "Programa.findByCodigo", query = "SELECT p FROM Programa p WHERE p.codigo = :codigo")
    , @NamedQuery(name = "Programa.findByNombrePrograma", query = "SELECT p FROM Programa p WHERE p.nombrePrograma = :nombrePrograma")})
public class Programa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Basic(optional = false)
    @Column(name = "nombre_programa")
    private String nombrePrograma;
    @Lob
    @Column(name = "img_programa")
    private byte[] imgPrograma;
    @JoinColumn(name = "director_programa", referencedColumnName = "codigo_docente")
    @ManyToOne(optional = false)
    private Docente directorPrograma;
    @JoinColumn(name = "departamento_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamento departamentoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programa")
    private List<Pensum> pensumList;

    public Programa() {
    }

    public Programa(Integer codigo) {
        this.codigo = codigo;
    }

    public Programa(Integer codigo, String nombrePrograma) {
        this.codigo = codigo;
        this.nombrePrograma = nombrePrograma;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public byte[] getImgPrograma() {
        return imgPrograma;
    }

    public void setImgPrograma(byte[] imgPrograma) {
        this.imgPrograma = imgPrograma;
    }

    public Docente getDirectorPrograma() {
        return directorPrograma;
    }

    public void setDirectorPrograma(Docente directorPrograma) {
        this.directorPrograma = directorPrograma;
    }

    public Departamento getDepartamentoId() {
        return departamentoId;
    }

    public void setDepartamentoId(Departamento departamentoId) {
        this.departamentoId = departamentoId;
    }

    @XmlTransient
    public List<Pensum> getPensumList() {
        return pensumList;
    }

    public void setPensumList(List<Pensum> pensumList) {
        this.pensumList = pensumList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Programa)) {
            return false;
        }
        Programa other = (Programa) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Programa[ codigo=" + codigo + " ]";
    }
    
}
