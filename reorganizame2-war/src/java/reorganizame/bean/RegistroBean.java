/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import reorganizame.ejb.UsuarioFacade;
import reorganizame.entity.Usuario;

/**
 *
 * @author David
 */
@Named(value = "registroBean")
@SessionScoped
public class RegistroBean implements Serializable {

    @Inject
    private UsuarioBean usuarioBean;

    @EJB
    private UsuarioFacade usuarioFacade;

    protected String nombre, apellidos, correo, alias, contrasena, contrasena2, mensajeRegistro;
    protected Date fechaNacimiento;

    /**
     * Creates a new instance of RegistroBean
     */
    public RegistroBean() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getContrasena2() {
        return contrasena2;
    }

    public void setContrasena2(String contrasena2) {
        this.contrasena2 = contrasena2;
    }

    public String getMensajeRegistro() {
        return mensajeRegistro;
    }

    public void setMensajeRegistro(String mensajeRegistro) {
        this.mensajeRegistro = mensajeRegistro;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String doRegistro() {
        String paginaReturn;
        this.mensajeRegistro = null;
        if (Util.isValidEmailAddress(this.correo)
                && this.alias.length() >= 4
                && this.contrasena.length() >= 4
                && this.contrasena.equals(this.contrasena2)) {
            Usuario usuarioParaRegistrar = new Usuario(0, this.correo, this.alias, Util.hash(this.contrasena));
            usuarioParaRegistrar.setNombre(this.nombre);
            usuarioParaRegistrar.setApellidos(this.apellidos);
            usuarioParaRegistrar.setFechaNacimiento(this.fechaNacimiento);
            try {
                this.usuarioFacade.create(usuarioParaRegistrar);
            } catch (EJBException ex) {
                this.mensajeRegistro = "Ya existe usuario con mismo alias o correo";
            }
        } else {
            this.mensajeRegistro = "Correo no válido, alias muy corto, contraseña muy corta o no coincide repetida";
        }
        if (this.mensajeRegistro == null) {
            this.limpiar();
            this.usuarioBean.setMensajeLogin("Usuario registrado con éxito");
            paginaReturn = "login";
        } else {
            paginaReturn = "registro";
        }
        return paginaReturn;
    }

    private void limpiar() {
        this.nombre = null;
        this.apellidos = null;
        this.fechaNacimiento = null;
        this.correo = null;
        this.alias = null;
        this.contrasena = null;
        this.contrasena2 = null;
        this.mensajeRegistro = null;
    }

    public String doEnlaceLogin() {
        this.limpiar();
        return "login";
    }

}
