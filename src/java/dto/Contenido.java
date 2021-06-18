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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "contenido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contenido.findAll", query = "SELECT c FROM Contenido c")
    , @NamedQuery(name = "Contenido.findById", query = "SELECT c FROM Contenido c WHERE c.id = :id")
    , @NamedQuery(name = "Contenido.findByTexto", query = "SELECT c FROM Contenido c WHERE c.texto = :texto")
    , @NamedQuery(name = "Contenido.findByCantidadItemsLista", query = "SELECT c FROM Contenido c WHERE c.cantidadItemsLista = :cantidadItemsLista")})
public class Contenido implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "texto")
    private String texto;
    @Basic(optional = false)
    @Column(name = "cantidad_items_lista")
    private int cantidadItemsLista;
    @JoinColumn(name = "seccion_microcurriculo_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SeccionMicrocurriculo seccionMicrocurriculoId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contenidoId")
    private List<TablaMicrocurriculoInfo> tablaMicrocurriculoInfoList;

    public Contenido() {
    }

    public Contenido(Integer id) {
        this.id = id;
    }

    public Contenido(Integer id, String texto, int cantidadItemsLista) {
        this.id = id;
        this.texto = texto;
        this.cantidadItemsLista = cantidadItemsLista;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getCantidadItemsLista() {
        return cantidadItemsLista;
    }

    public void setCantidadItemsLista(int cantidadItemsLista) {
        this.cantidadItemsLista = cantidadItemsLista;
    }

    public SeccionMicrocurriculo getSeccionMicrocurriculoId() {
        return seccionMicrocurriculoId;
    }

    public void setSeccionMicrocurriculoId(SeccionMicrocurriculo seccionMicrocurriculoId) {
        this.seccionMicrocurriculoId = seccionMicrocurriculoId;
    }

    @XmlTransient
    public List<TablaMicrocurriculoInfo> getTablaMicrocurriculoInfoList() {
        return tablaMicrocurriculoInfoList;
    }

    public void setTablaMicrocurriculoInfoList(List<TablaMicrocurriculoInfo> tablaMicrocurriculoInfoList) {
        this.tablaMicrocurriculoInfoList = tablaMicrocurriculoInfoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contenido)) {
            return false;
        }
        Contenido other = (Contenido) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Contenido[ id=" + id + " ]";
    }
    
}
