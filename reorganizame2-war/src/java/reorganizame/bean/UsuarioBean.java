/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import reorganizame.ejb.InvitacionFacade;
import reorganizame.ejb.MiembroFacade;
import reorganizame.ejb.UsuarioFacade;
import reorganizame.entity.Invitacion;
import reorganizame.entity.Miembro;

import reorganizame.entity.Proyecto;
import reorganizame.entity.Usuario;

/**
 *
 * @author Alejandro Reyes
 */
@Named(value = "usuarioBean")
@SessionScoped
public class UsuarioBean implements Serializable {

    @EJB
    private UsuarioFacade usuarioFacade;
    
    @EJB
    private InvitacionFacade invitacionFacade;

    @EJB
    private MiembroFacade miembroFacade;
        
    protected Usuario usuarioActual;
    protected Proyecto proyectoSeleccionado;
    protected String alias, contrasena, mensajeLogin, 
            mensajeRecuperar, emailRecuperar, 
            contrasenaAntiguaPerfil, contrasenaNuevaPerfil, contrasenaNueva2Perfil, mensajePerfil, mensajeInvitacion;

    /**
     * Creates a new instance of UsuarioBean
     */
    public UsuarioBean() {
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public Proyecto getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    public void setProyectoSeleccionado(Proyecto proyectoSeleccionado) {
        this.proyectoSeleccionado = proyectoSeleccionado;
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

    public String getMensajeRecuperar() {
        return mensajeRecuperar;
    }

    public void setMensajeRecuperar(String mensajeRecuperar) {
        this.mensajeRecuperar = mensajeRecuperar;
    }

    public String getEmailRecuperar() {
        return emailRecuperar;
    }

    public void setEmailRecuperar(String emailRecuperar) {
        this.emailRecuperar = emailRecuperar;
    }

    public String getContrasenaAntiguaPerfil() {
        return contrasenaAntiguaPerfil;
    }

    public void setContrasenaAntiguaPerfil(String contrasenaAntiguaPerfil) {
        this.contrasenaAntiguaPerfil = contrasenaAntiguaPerfil;
    }

    public String getContrasenaNuevaPerfil() {
        return contrasenaNuevaPerfil;
    }

    public void setContrasenaNuevaPerfil(String contrasenaNuevaPerfil) {
        this.contrasenaNuevaPerfil = contrasenaNuevaPerfil;
    }

    public String getContrasenaNueva2Perfil() {
        return contrasenaNueva2Perfil;
    }

    public void setContrasenaNueva2Perfil(String contrasenaNueva2Perfil) {
        this.contrasenaNueva2Perfil = contrasenaNueva2Perfil;
    }

    public String getMensajePerfil() {
        return mensajePerfil;
    }

    public void setMensajePerfil(String mensajePerfil) {
        this.mensajePerfil = mensajePerfil;
    }

    public String getMensajeInvitacion() {
        return mensajeInvitacion;
    }

    public void setMensajeInvitacion(String mensajeInvitacion) {
        this.mensajeInvitacion = mensajeInvitacion;
    }

    public String doLogin() {
        String paginaReturn;
        Usuario usuario = this.usuarioFacade.findUsuarioByAlias(this.alias);
        if (usuario != null && usuario.getContrasena().equals(Util.hash(this.contrasena))) {
            this.usuarioActual = usuario;
            this.limpiarLogin();
            paginaReturn = "listaProyectos";
        } else {
            this.usuarioActual = null; //si hay un nuevo intento de sesión el usuario antiguo conectado se pierde
            this.limpiarLogin();
            this.mensajeLogin = "Alias o contraseña incorrecto";
            paginaReturn = "login";
        }
        return paginaReturn;
    }

    private void limpiarLogin() {
        this.alias = null;
        this.contrasena = null;
        this.mensajeLogin = null;
    }

    public String doEnlaceRegistro() {
        this.limpiarLogin();
        return "registro";
    }

    public String doEnlaceRecuperarPassword() {
        this.limpiarLogin();
        this.mensajeRecuperar = "Introduzca su correo electrónico para enviarle sus datos sobre el inicio de sesión.";
        return "recuperar";
    }

    public String doProcesarRecuperacion() {
        String siguientePagina;
        if (Util.isValidEmailAddress(this.emailRecuperar)) {
            Usuario usuario = this.usuarioFacade.findUserByEmail(this.emailRecuperar);
            if (usuario!=null){
                this.enviarMensaje(usuario);
                siguientePagina = "passwordRecuperada";
            } else {
                this.mensajeRecuperar = "No existen usuarios con ese correo.";
                siguientePagina = "recuperar";
            }
        } else {
            this.mensajeRecuperar = "Dirección de correo errónea, introduzca su correo de nuevo.";
            siguientePagina = "recuperar";
        }
        return siguientePagina;
    }

    private void enviarMensaje(Usuario usuario) {
        final String username = "reorganizame.app@gmail.com";
        final String pass = "maqkmsvbwttgxtfb";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, pass);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("reorganiza.me@gmail.com", "Reorganiza.me"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getCorreo(), usuario.getNombre()));
            msg.setSubject("Ha recuperado su contraseña");
            String password = this.generarPasswordAleatorio(10);
            usuario.setContrasena(Util.hash(password));
            this.usuarioFacade.edit(usuario);
            msg.setText("Su contraseña ha sido reestablecida.\nLa nueva contraseña es: " + password);
            Transport.send(msg);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String generarPasswordAleatorio(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public String doVolverPrincipal() {
        return "listaProyectos";
    }
    
    public String doVolverDeRecuperacionPasswordAlLogin(){
        this.emailRecuperar = null;
        this.mensajeRecuperar = null;
        return "login";
    }
    
    public String doGuardarPerfil(){
        this.mensajePerfil = null;
        if (!Util.isValidEmailAddress(this.usuarioActual.getCorreo()) || !(this.usuarioActual.getAlias().length()>=4)){
            this.mensajePerfil = "Correo no valido o alias muy corto";
        } else {
            if (!this.contrasenaAntiguaPerfil.equals("")){
                if (this.contrasenaNuevaPerfil.length() >= 4
                        && Util.hash(this.contrasenaAntiguaPerfil).equals(this.usuarioActual.getContrasena())
                        && this.contrasenaNuevaPerfil.equals(this.contrasenaNueva2Perfil)) {
                    this.usuarioActual.setContrasena(Util.hash(this.contrasenaNuevaPerfil));
                } else {
                    this.mensajePerfil = "Contraseña no cambiada: antigua incorrecta, nueva muy corta o no coincide repetida";
                }
            }
            if (this.mensajePerfil==null){
                try {
                    this.usuarioFacade.edit(this.usuarioActual);
                } catch (EJBException e){
                    this.mensajePerfil = "Nuevo alias o correo ya en uso";
                }
            }
        }
        if (this.mensajePerfil==null){
            this.mensajePerfil = "Cambios guardados con éxito";
        } else {
            this.usuarioActual = this.usuarioFacade.find(this.usuarioActual.getIdUsuario());
        }
        this.contrasenaAntiguaPerfil = null;
        this.contrasenaNuevaPerfil = null;
        this.contrasenaNueva2Perfil = null;
        return "perfil";
    }
    
    public String doIrAlPerfil(){
        return "perfil";
    }
    
    public String doVolverDePerfilAListaProyectos(){
        this.mensajePerfil = null;
        this.mensajeInvitacion = null;
        return "listaProyectos";
    }
    
    public String doAceptarInvitacion(Invitacion invitacion){
        Miembro miembroNuevo = new Miembro();
        miembroNuevo.setIdUsuario(this.usuarioActual);
        miembroNuevo.setIdProyecto(invitacion.getIdProyecto());
        miembroNuevo.setRol("Invitado");
        this.miembroFacade.create(miembroNuevo);
        this.usuarioActual.getMiembroCollection().add(miembroNuevo);
        this.invitacionFacade.remove(invitacion);
        this.usuarioActual.getInvitacionCollection().remove(invitacion);
        this.mensajeInvitacion = "Invitación aceptada al proyecto " + invitacion.getIdProyecto().getNombre();
        return "perfil";
    }
    
    public String doRechazarInvitacion(Invitacion invitacion){
        this.invitacionFacade.remove(invitacion);
        this.usuarioActual.getInvitacionCollection().remove(invitacion);
        this.mensajeInvitacion = "Invitación rechazada al proyecto " + invitacion.getIdProyecto().getNombre();
        return "perfil";
    }
    
    public String doLogout(){
        this.usuarioActual = null;
        this.proyectoSeleccionado = null;
        return "login";
    }

}
