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
import reorganizame.entity.Miembro;
import reorganizame.entity.Usuario;

/**
 *
 * @author David
 */
@Stateless
public class MiembroFacade extends AbstractFacade<Miembro> {

    @PersistenceContext(unitName = "reorganizame-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MiembroFacade() {
        super(Miembro.class);
    }

    public List<Miembro> findMiembroByUsuario(Usuario usr) {
        Query q;
        q = em.createQuery("SELECT m FROM Miembro m WHERE m.idUsuario = :Usuario AND m.idProyecto.lider !=:Usuario");
        q.setParameter("Usuario", usr);
        return q.getResultList();
    }

}
