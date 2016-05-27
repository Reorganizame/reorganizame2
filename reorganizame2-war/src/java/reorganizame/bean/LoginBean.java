/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import reorganizame.ejb.UsuarioFacade;
import reorganizame.entity.Usuario;

/**
 *
 * @author David
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    @EJB
    private UsuarioFacade usuarioFacade;
    
    protected String alias, contrasena, mensajeLogin;
    protected Usuario usuario;

    /**
     * Creates a new instance of LoginBean
     */
    public LoginBean() {
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
    
    public String getMensajeLogin() {
        return mensajeLogin;
    }

    public void setMensajeLogin(String mensajeLogin) {
        this.mensajeLogin = mensajeLogin;
    }
    
    public String doLogin(){
        String paginaReturn;
        this.usuario = this.usuarioFacade.findUsuarioByAlias(this.alias);
        if (this.usuario!=null && this.usuario.getContrasena().equals(Util.hash(this.contrasena))){
            this.limpiar();
            paginaReturn = "listaProyectos";
        } else {
            this.alias = null;
            this.contrasena = null;
            this.mensajeLogin = "Alias o contraseña incorrecto";
            paginaReturn = "login";
        }
        return paginaReturn;
    }
    
    private void limpiar(){
        this.alias = null;
        this.contrasena = null;
        this.mensajeLogin = null;
    }
    
    public String doEnlaceRegistro(){
        this.limpiar();
        return "registro";
    }
    
    public String doEnlaceRecuperarPassword(){
        this.limpiar();
        return "recuperarPassword";
    }
    
}
