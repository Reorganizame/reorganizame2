/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import reorganizame.ejb.CategoriaFacade;
import reorganizame.ejb.TareaFacade;
import reorganizame.entity.Categoria;
import reorganizame.entity.Tarea;

/**
 *
 * @author Lenovo
 */
@Named(value = "tareaBean")
@SessionScoped
public class TareaBean implements Serializable {

    @EJB
    private TareaFacade tareaFacade;
    @EJB
    private CategoriaFacade categoriaFacade;

    protected List<Tarea> listaTareas;
    protected List<Categoria> listaCategorias;
    protected Tarea tareaSeleccionada;
    protected Integer idCategoriaSeleccionada;

    @Inject
    private UsuarioBean usuarioBean;

    public TareaBean() {
    }

    @PostConstruct
    public void init() {
        listaTareas = new ArrayList<>();
        doCargarTareas();
        listaCategorias = categoriaFacade.findAll();
        idCategoriaSeleccionada = listaCategorias.get(0).getIdCategoria();
    }

    public List<Tarea> getListaTareas() {
        return this.listaTareas;
    }

    public void setListaTareas(List<Tarea> lista) {
        this.listaTareas = lista;
    }

    public List<Categoria> getListaCategorias() {
        return this.listaCategorias;
    }

    public Tarea getTareaSeleccionada() {
        return this.tareaSeleccionada;
    }

    public void setTareaSeleccionada(Tarea tarea) {
        this.tareaSeleccionada = tarea;
    }

    public Integer getIdCategoriaSeleccionada() {
        return idCategoriaSeleccionada;
    }

    public void setIdCategoriaSeleccionada(Integer idCategoriaSeleccionada) {
        this.idCategoriaSeleccionada = idCategoriaSeleccionada;
    }

    public String doCargarTareas() {
        listaTareas = tareaFacade.tareasDeUnProyecto(usuarioBean.getProyectoSeleccionado().getIdProyecto());
        return "listaTareas";
    }

    public String doNuevaTarea() {
        this.reset();
        return "editarTarea";
    }

    public void reset() {
        this.tareaSeleccionada = new Tarea();
    }

    public String doGuardarTarea() {
        this.tareaSeleccionada.setCategoria(this.categoriaFacade.find(this.idCategoriaSeleccionada));
        if (this.tareaSeleccionada.getIdTarea() != null) {
            this.tareaFacade.edit(tareaSeleccionada);
        } else {
            this.tareaSeleccionada.setIdTarea(0);
            this.tareaSeleccionada.setAutor(usuarioBean.getUsuarioActual());
            this.tareaSeleccionada.setProyecto(usuarioBean.getProyectoSeleccionado());
            this.tareaFacade.create(tareaSeleccionada);
            this.listaTareas.add(tareaSeleccionada);
        }
        return "listaTareas";
    }

    public String doBorrar(Tarea tarea) {
        this.tareaFacade.remove(tarea);
        this.listaTareas.remove(tarea);
        return "listaTareas";
    }

    public String doEditar(Tarea tarea) {
        this.tareaSeleccionada = tarea;
        this.idCategoriaSeleccionada = tarea.getCategoria().getIdCategoria();
        return "editarTarea";
    }

    public String doVolver() {
        return "proyectoConcreto";
    }

    public String doVolverLista() {
        return "listaTareas";
    }

}
