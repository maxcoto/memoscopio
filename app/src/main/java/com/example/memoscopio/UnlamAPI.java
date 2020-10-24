package com.example.memoscopio;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class UnlamAPI extends AsyncTask<String, String, String> {

    private JSONObject data;
    private HttpURLConnection request;
    private URL url;
    private DataOutputStream out;
    private DataInputStream in;

    private UnlamCallback callback;

    public UnlamAPI(UnlamCallback callback){
        this.data = new JSONObject();
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            data.put("env", "TEST");
            data.put("name", "maxi");
            data.put("lastname", "perez");
            data.put("dni", "35360791");
            data.put("email", "maxi@maxi.com");
            data.put("password", "12345678");
            data.put("commission", "123");

            url = new URL("http://so-unlam.net.ar/api/api/register");
            request = (HttpURLConnection) url.openConnection();
            request.setDoOutput(true);
            request.setDoInput(true);
            request.setRequestMethod("POST");
            request.setRequestProperty("content-type", "application/json");
            request.setChunkedStreamingMode(0);

            in = new DataInputStream(request.getInputStream());
            out = new DataOutputStream(request.getOutputStream());
            out.writeBytes(data.toString());

            request.connect();
            int respuestaServidor = request.getResponseCode(); // obtengo la

            out.flush();
            out.close();

            int a = 0;
            if(respuestaServidor==200)
                a = 1;
            else
                a = 2;

            System.out.println("hola sistemas operativos");
            System.out.println(in);

            //activity.callback(in);
            callback.onUnlamCallback(in.toString());

            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            //writer.writeBytes(paquete.toString());
            //writer.write(String.valueOf(data));
            //writer.flush();
            //writer.close();
            //out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            callback.onUnlamCallback(e.getMessage());
        } finally {
            request.disconnect();
        }

        return "";
    }
}