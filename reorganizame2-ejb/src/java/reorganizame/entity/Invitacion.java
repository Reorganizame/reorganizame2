/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author David
 */
@Entity
@Table(name = "invitacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invitacion.findAll", query = "SELECT i FROM Invitacion i"),
    @NamedQuery(name = "Invitacion.findByIdInvitacion", query = "SELECT i FROM Invitacion i WHERE i.idInvitacion = :idInvitacion")})
public class Invitacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idInvitacion")
    private Integer idInvitacion;
    @JoinColumn(name = "idProyecto", referencedColumnName = "idProyecto")
    @ManyToOne(optional = false)
    private Proyecto idProyecto;
    @JoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public Invitacion() {
    }

    public Invitacion(Integer idInvitacion) {
        this.idInvitacion = idInvitacion;
    }

    public Integer getIdInvitacion() {
        return idInvitacion;
    }

    public void setIdInvitacion(Integer idInvitacion) {
        this.idInvitacion = idInvitacion;
    }

    public Proyecto getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Proyecto idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInvitacion != null ? idInvitacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invitacion)) {
            return false;
        }
        Invitacion other = (Invitacion) object;
        if ((this.idInvitacion == null && other.idInvitacion != null) || (this.idInvitacion != null && !this.idInvitacion.equals(other.idInvitacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "reorganizame.entity.Invitacion[ idInvitacion=" + idInvitacion + " ]";
    }
    
}
