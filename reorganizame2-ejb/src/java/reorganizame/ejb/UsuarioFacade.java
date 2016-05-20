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
import reorganizame.entity.Usuario;

/**
 *
 * @author David
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "reorganizame-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario findUserByEmail(String email) {
        Query q = em.createNamedQuery("Usuario.findByCorreo");
        q.setParameter("correo", email);
        List<Usuario> listausuarios = q.getResultList();
        Usuario resultado = null;
        if (!listausuarios.isEmpty()) {
            resultado = listausuarios.get(0);
        }
        return resultado;
    }

    public Usuario findUsuarioByAlias(String alias) {
        Query consulta = this.em.createNamedQuery("Usuario.findByAlias");
        consulta.setParameter("alias", alias);
        List<Usuario> resultadoConsulta = consulta.getResultList();
        Usuario usuarioEncontrado = null;
        if (!resultadoConsulta.isEmpty()) {
            usuarioEncontrado = resultadoConsulta.get(0);
        }
        return usuarioEncontrado;
    }

    public List<Usuario> usuariosNoMiembrosDeUnProyecto(int idProyecto) {
        Query consulta;
        consulta = this.em.createQuery("SELECT u FROM Usuario u, Proyecto p WHERE u.idUsuario NOT IN"
                + " (SELECT m.idUsuario.idUsuario FROM Miembro m WHERE m.idProyecto.idProyecto=:idProyecto) "
                + " AND u.idUsuario != p.lider.idUsuario AND p.idProyecto=:idProyecto2");
        consulta.setParameter("idProyecto2", idProyecto);
        consulta.setParameter("idProyecto", idProyecto);

        /*
        consulta = this.em.createQuery("SELECT u FROM Usuario u WHERE u.idUsuario NOT IN"
                + " (SELECT m.idUsuario.idUsuario FROM Miembro m WHERE m.idProyecto.idProyecto=:idProyecto) "
                + "AND u.idUsuario NOT IN (SELECT p.lider.idUsuario FROM Proyecto p WHERE p.idProyecto:=idProyecto2)");
         */
        return consulta.getResultList();
    }
}
