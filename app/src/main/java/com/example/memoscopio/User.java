package com.example.memoscopio;

import org.json.JSONObject;

public class User {

    private String name;
    private String lastname;
    private String dni;
    private String email;
    private String password;
    private String commission;

    public static String token = "";
    public static String token_refresh = "";

    public User(String name, String lastname, String dni, String email, String password, String commission){
        this.name = name;
        this.lastname = lastname;
        this.dni = dni;
        this.email = email;
        this.password = password;
        this.commission = commission;
    }

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    protected String registerData(){
        JSONObject user = new JSONObject();
        try {
            user.put("name", name);
            user.put("lastname", lastname);
            user.put("dni", dni);
            user.put("email", email);
            user.put("password", password);
            user.put("commission", commission);
        } catch (Exception e){
            e.printStackTrace();
        }

        return user.toString();
    }

    protected String loginData(){
        JSONObject user = new JSONObject();
        try {
            user.put("email", email);
            user.put("password", password);
        } catch (Exception e){
            e.printStackTrace();
        }

        return user.toString();
    }

    protected String validateRegister(){
        if (name.length() == 0) {
            return("El nombre no puede estar en blanco");
        }

        if (lastname.length() == 0) {
            return("El apellido no puede estar en blanco");
        }

        if (dni.length() == 0) {
            return("El DNI no puede estar en blanco");
        }

        if (email.length() == 0) {
            return("El email no puede estar en blanco");
        }

        if (!email.matches(Constants.EMAIL_PATTERN)) {
            return("Formato de email invalido");
        }

        if (password.length() < 8) {
            return("La longitud del password debe ser de 8 caracteres como minimo");
        }

        if (commission.length() == 0) {
            return("La comision no puede estar en blanco");
        }

        return "ok";
    }

    protected String validateLogin(){
        if (email.length() == 0) {
            return("El email no puede estar en blanco");
        }

        if (!email.matches(Constants.EMAIL_PATTERN)) {
            return("Formato de email invalido");
        }

        if (password.length() < 8) {
            return("La longitud del password debe ser de 8 caracteres como minimo");
        }

        return "ok";
    }


}
