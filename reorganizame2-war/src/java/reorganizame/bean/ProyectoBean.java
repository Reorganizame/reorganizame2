/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.inject.Inject;
import reorganizame.ejb.InvitacionFacade;
import reorganizame.ejb.MiembroFacade;
import reorganizame.ejb.ProyectoFacade;
import reorganizame.ejb.UsuarioFacade;
import reorganizame.entity.Invitacion;
import reorganizame.entity.Miembro;
import reorganizame.entity.Proyecto;
import reorganizame.entity.Usuario;

/**
 *
 * @author alumno
 */
@Named(value = "proyectoBean")
@SessionScoped
public class ProyectoBean implements Serializable {

    @EJB
    private ProyectoFacade pFacade;

    @EJB
    private MiembroFacade mFacade;

    @EJB
    private InvitacionFacade invitacionFacade;

    @EJB
    private UsuarioFacade usuarioFacade;

    protected List<Proyecto> listaMisProyectos;
    protected List<Proyecto> listaProyectos;

    protected List<Usuario> listaUsuariosAInvitar;
    protected List<Integer> listaIdUsuarioInvitado;

    @Inject
    private UsuarioBean usuarioBean;

    /**
     * Creates a new instance of ProyectoBean
     */
    public ProyectoBean() {
    }

    @PostConstruct
    public void init() {
        listaMisProyectos = new ArrayList<>();
        listaProyectos = new ArrayList<>();
        listaMisProyectos = pFacade.findProyectoByLider(usuarioBean.getUsuarioActual());
        listaProyectos.clear();
        List<Miembro> ms;
        ms = mFacade.findMiembroByUsuario(usuarioBean.getUsuarioActual());
        for (Miembro m : ms) {
            listaProyectos.add(m.getIdProyecto());
        }
    }

    public List<Proyecto> getListaMisProyectos() {
        return listaMisProyectos;
    }

    public void setListaMisProyectos(List<Proyecto> listaMisProyectos) {
        this.listaMisProyectos = listaMisProyectos;
    }

    public List<Proyecto> getListaProyectos() {
        return listaProyectos;
    }

    public void setListaProyectos(List<Proyecto> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }

    public List<Usuario> getListaUsuariosAInvitar() {
        return listaUsuariosAInvitar;
    }

    public void setListaUsuariosAInvitar(List<Usuario> listaUsuariosAInvitar) {
        this.listaUsuariosAInvitar = listaUsuariosAInvitar;
    }

    public List<Integer> getListaIdUsuarioInvitado() {
        return listaIdUsuarioInvitado;
    }

    public void setListaIdUsuarioInvitado(List<Integer> listaIdUsuarioInvitado) {
        this.listaIdUsuarioInvitado = listaIdUsuarioInvitado;
    }

    public String doNuevo() {
        this.reset();
        return "crearNuevoProyecto";
    }

    private void reset() {
        usuarioBean.setProyectoSeleccionado(new Proyecto()); //ahora cambia el de usuario
    }

    public String doGuardar() {
        Proyecto p = usuarioBean.getProyectoSeleccionado();//en el formulario se asigna Nombre y descripción
        p.setIdProyecto(0);
        Date fechaInicio = new Date();
        p.setFechaInicio(fechaInicio);
        p.setLider(usuarioBean.getUsuarioActual());
        this.pFacade.create(p);
        usuarioBean.setProyectoSeleccionado(p);

        Miembro m = new Miembro(0);
        m.setIdMiembro(0);
        m.setIdUsuario(usuarioBean.getUsuarioActual());
        m.setIdProyecto(usuarioBean.getProyectoSeleccionado());
        m.setRol("Director de proyecto");
        mFacade.create(m);

        return "listaProyectos";
    }

    public String doAcceder(Proyecto p) {
        usuarioBean.setProyectoSeleccionado(p); //usuario
        listaUsuariosAInvitar = new ArrayList<>();
        listaIdUsuarioInvitado = new ArrayList<>();

        cargarUsuariosAInvitar();

        return "proyectoConcreto";
    }

    public String doInvitar() {
        for (int idUsuario : listaIdUsuarioInvitado) {
            Invitacion nuevaInvitacion = new Invitacion(0);
            nuevaInvitacion.setIdUsuario(usuarioFacade.find(idUsuario));
            nuevaInvitacion.setIdProyecto(usuarioBean.getProyectoSeleccionado());
            invitacionFacade.create(nuevaInvitacion);
        }
        this.cargarUsuariosAInvitar();
        return "proyectoConcreto";
    }

    private void cargarUsuariosAInvitar() {
        List<Usuario> listaNoMiembros = usuarioFacade.usuariosNoMiembrosDeUnProyecto(usuarioBean.getProyectoSeleccionado().getIdProyecto());
        List<Integer> listaIdNoMiembros = new ArrayList<>();
        for (Usuario u : listaNoMiembros) {
            listaIdNoMiembros.add(u.getIdUsuario());
        }
        listaUsuariosAInvitar = usuarioFacade.usuariosSinInvitacionAUnProyecto(listaIdNoMiembros, usuarioBean.getProyectoSeleccionado().getIdProyecto());
        this.listaIdUsuarioInvitado = new ArrayList<>();
    }

}
