package com.example.memoscopio;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Connection {

    public Connection() { }

    public static boolean check(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        // verifica la conexion a internet
        boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // en caso de error muestra un error en pantalla en la activity que la invocó mediante el context
        if(!connected) {
            Toast.makeText(context, "No hay conexion a internet", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
