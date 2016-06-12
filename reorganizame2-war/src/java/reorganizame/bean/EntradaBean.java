/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.inject.Inject;
import reorganizame.ejb.EntradaFacade;
import reorganizame.entity.Entrada;

/**
 *
 * @author nacho
 */
@Named(value = "entradaBean")
@SessionScoped
public class EntradaBean implements Serializable {

    @Inject
    private UsuarioBean usuarioBean;

    @EJB
    private EntradaFacade entradaFacade;

    protected Entrada entradaSeleccionada;
    protected List<Entrada> listaEntradas;
    protected List<Entrada> listaMostradas;

    public EntradaBean() {
    }

    @PostConstruct
    public void init() {
        listaEntradas = entradaFacade.entradasDeUnProyecto(usuarioBean.getProyectoSeleccionado().getIdProyecto());
        listaMostradas = new ArrayList<>();
        int i = 0;
        while (i < 10 && i < listaEntradas.size()) {
            listaMostradas.add(listaEntradas.get(i));
            i++;
        }
    }

    public Entrada getEntradaSeleccionada() {
        return entradaSeleccionada;
    }

    public void setEntradaSeleccionada(Entrada entradaSeleccionada) {
        this.entradaSeleccionada = entradaSeleccionada;
    }

    public List<Entrada> getListaEntradas() {
        return listaEntradas;
    }

    public void setListaEntradas(List<Entrada> listaEntradas) {
        this.listaEntradas = listaEntradas;
    }

    public List<Entrada> getListaMostradas() {
        return listaMostradas;
    }

    public void setListaMostradas(List<Entrada> listaMostradas) {
        this.listaMostradas = listaMostradas;
    }

    public String doNuevo() {
        this.reset();
        return "editarEntrada";
    }

    public void reset() {
        this.setEntradaSeleccionada(new Entrada());
    }

    public String doCargarMas() {
        return "entradas";
    }

    public String doGuardar() {
        Entrada e = this.getEntradaSeleccionada();//en el formulario se asigna Nombre y contenido
        if (e.getIdEntrada() != null) {
            this.entradaFacade.edit(e);
        } else {
            e.setIdEntrada(0); //se le asigna id 0
            e.setProyecto(usuarioBean.getProyectoSeleccionado()); //se le asigna proyecto seleccionado
            e.setCreador(usuarioBean.getUsuarioActual());
            e.setFecha(Calendar.getInstance().getTime());
            this.entradaFacade.create(e); //se crea la entrada
        }
        return "proyectoConcreto";
    }

    public String doCancelar() {
        return "proyectoConcreto";
    }

    public String fechaDeEntrada(Entrada entrada) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String stringDeFecha;
        stringDeFecha = simpleDateFormat.format(entrada.getFecha());
        return stringDeFecha;
    }

    public String doEditar(Entrada e) {
        this.entradaSeleccionada = e;
        return "editarEntrada";
    }

    public String doBorrar(Entrada e) {
        this.entradaFacade.remove(e);
        listaEntradas.remove(e);
        listaMostradas.remove(e);
        return null;
    }

}
