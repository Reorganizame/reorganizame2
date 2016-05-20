/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorganizame.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author David
 */
public class Util {

    public static String hash(String cadena) {
        String hash;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(cadena.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest()) {
                sb.append(String.format("%02X", b));
            }
            hash = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            hash = null;
        }
        return hash;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static Date fechaDesdeString(String stringDeFecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaResultado;
        try {
            fechaResultado = simpleDateFormat.parse(stringDeFecha);
        } catch (ParseException ex) {
            fechaResultado = null;
        }
        return fechaResultado;
    }

    public static String stringDesdeFecha(Date fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String stringDeFecha;
        stringDeFecha = simpleDateFormat.format(fecha);
        return stringDeFecha;
    }

}
