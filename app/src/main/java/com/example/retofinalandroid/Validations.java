package com.example.retofinalandroid;

import android.content.Context;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Validations {

    private Modelo mod;
    private Context appContext;

    public Validations(Modelo mod, Context appContext) {
        this.mod = mod;
        this.appContext = appContext;
    }

    /**
     * Check if a DNI string is valid
     * @param dni
     * @return
     */
    public boolean validateDNI(String dni) {
        boolean esValido = false;
        int i = 0;
        int caracterASCII = 0;
        char letra = ' ';
        int miDNI = 0;
        int resto = 0;
        char[] asignacionLetra = {'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X','B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};
        if(dni.length() == 9 && Character.isLetter(dni.charAt(8))) {
            while(i < dni.length() - 1) {
                caracterASCII = dni.codePointAt(i);
                esValido = (caracterASCII > 47 && caracterASCII < 58);
                if(!esValido) {
                    return esValido;
                }
                i++;
            }
        }
        if(esValido) {
            letra = Character.toUpperCase(dni.charAt(8));
            miDNI = Integer.parseInt(dni.substring(0,8));
            resto = miDNI % 23;
            esValido = (letra == asignacionLetra[resto]);
        }
        return esValido;
    }

    /**
     * Check if an email string is valid
     * @param email
     * @return
     */
    public boolean validateEmail(String email) {
        String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        return email.matches(regex);
    }

    /**
     * Check if a telephone string is valid
     * @param telephone
     * @return
     */
    public boolean validatePhoneNumber(String telephone) {
        String regex = "^((\\+|00)(\\d{2}))?-?\\d{3}-?(\\d{4}|\\d{3})-?(\\d{4}|\\d{3})$";
        return telephone.matches(regex);
    }

    /**
     * Check if an user with especified dni or email already exists
     * @param dni
     * @param email
     * @return
     */
    public boolean checkUserExist(String dni, String email){
        ArrayList<Usuario> usuarios = mod.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getDni().equals(dni) || usuarios.get(i).getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public Usuario checkUserCredentials(String email, String password){
        ArrayList<Usuario> usuarios = mod.getUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getEmail().equals(email) && usuarios.get(i).getContrasena().equals(password)) {
                return usuarios.get(i);
            }
        }
        return null;
    }

    /**
     * Check if old and new passwords meet the requirements to do a password update
     * @return
     */
    public boolean newPasswordValidation(String currentPassword, String oldPassword, String newPassword) {
        if (oldPassword.equals("")) {
            Toast.makeText(appContext, R.string.empty_old_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (newPassword.equals("")) {
            Toast.makeText(appContext, R.string.empty_new_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (oldPassword.equals(newPassword)) {
            Toast.makeText(appContext, R.string.same_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!passwordHashing(oldPassword).equals(currentPassword)) {
            Toast.makeText(appContext, R.string.wrong_old_pass, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Metodo que se ancarga de encriptar la contraseña
     * @param password Contraseña que se quiere encriptar
     * @return Retorna la contraseña encriptada
     */
    public String passwordHashing(String password){
        String generatedPassword = null;
        try {
            // Crea una instancia de MessageDigest para MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            // Agrega la contraseña separada en bytes para separarla
            md.update(password.getBytes());
            // Saca los bytes separados (se almacena los bytes en formato decimal)
            byte[] bytes = md.digest();
            // Los bytes en decimal pasan a hexadecimal
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            // Coge los bytes separados de la contraseña en hexadecimal y los junta en un string
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

}
