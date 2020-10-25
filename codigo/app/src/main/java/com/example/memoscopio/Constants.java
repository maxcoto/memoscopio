package com.example.memoscopio;

public class Constants {

    public static final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static final String BASE_URI = "http://so-unlam.net.ar/api/api/";
    public static final String REGISTER_URI = BASE_URI + "register";
    public static final String LOGIN_URI = BASE_URI + "login";
    public static final String EVENT_URI = BASE_URI + "event";
    public static final String REFRESH_URI = BASE_URI + "refresh";

    public static final String BASE_RANKING_URI = "http://rankingapi.herokuapp.com/";
    public static final String RANKING_GET_URI = BASE_RANKING_URI + "ranking/get";
    public static final String RANKING_SET_URI = BASE_RANKING_URI + "ranking/set";

    public static final String ENVIRONMENT = "PROD";

    public static final String INDEX_PREFERENCE = "index";
    public static final String STORE_PREFERENCE = "xyzp";

}
