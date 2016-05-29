/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import reorganizame.ejb.MiembroFacade;
import reorganizame.ejb.ProyectoFacade;
import reorganizame.entity.Miembro;
import reorganizame.entity.Proyecto;

/**
 *
 * @author alumno
 */
@Named(value = "proyectoBean")
@Dependent

public class ProyectoBean {

    @EJB
    private ProyectoFacade pFacade;

    @EJB
    private MiembroFacade mFacade;

    protected List<Proyecto> listaMisProyectos;
    protected List<Proyecto> listaProyectos;

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
        doCargarProyectos();
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

    public String doCargarProyectos() {
        listaMisProyectos = pFacade.findProyectoByLider(usuarioBean.getUsuarioActual());
        listaProyectos.clear();
        List<Miembro> ms;
        ms = mFacade.findMiembroByUsuario(usuarioBean.getUsuarioActual());
        for (Miembro m : ms) {
            listaProyectos.add(m.getIdProyecto());
        }
        return "listaProyectos";
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

        String retorno = doCargarProyectos(); //carga todos los proyectos de nuevo
        return retorno;

    }

    public String doAcceder(Proyecto p) {
        usuarioBean.setProyectoSeleccionado(p); //usuario
        return "proyectoConcreto";
    }

}
