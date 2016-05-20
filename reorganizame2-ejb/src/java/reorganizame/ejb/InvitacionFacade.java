/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.ejb;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import reorganizame.entity.Invitacion;
import reorganizame.entity.Usuario;

/**
 *
 * @author David
 */
@Stateless
public class InvitacionFacade extends AbstractFacade<Invitacion> {

    @PersistenceContext(unitName = "reorganizame-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvitacionFacade() {
        super(Invitacion.class);
    }

    public List<Invitacion> findByUsuario(Usuario usuario) {
        Query consulta = this.em.createQuery("SELECT i FROM Invitacion i WHERE i.idUsuario = :usuario");
        consulta.setParameter("usuario", usuario);
        return consulta.getResultList();
    }

}
