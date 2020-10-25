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

        boolean connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(!connected) {
            Toast.makeText(context, "No hay conexion a internet", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
