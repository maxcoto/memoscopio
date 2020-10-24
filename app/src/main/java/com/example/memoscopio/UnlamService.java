package com.example.memoscopio;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class UnlamService extends IntentService {

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.memoscopio.action.FOO";
    private static final String ACTION_BAZ = "com.example.memoscopio.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.memoscopio.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.memoscopio.extra.PARAM2";


    private Exception mException=null;
    private URL mUrl;

    public UnlamService() {
        super("UnlamService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String uri = intent.getExtras().getString("uri");
            JSONObject datosJson = new JSONObject(intent.getExtras().getString("datosJson"));

            ejecutarPost(uri, datosJson);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void ejecutarPost(String uri, JSONObject datosJson){
        String result = POST(uri,datosJson);

        if(result==null){
            Log.e("LOGUEO_SERVICE","Error en Get:\n"+mException.toString());
            return;
        }
        if (result== "NO_OK"){
            Log.e("LOGUEO_SERVICE","SE RECIBIO RESPONSE NO_OK");
            return;
        }

        //Falta completar
        Intent i = new Intent("com.example.memoscopio.action.RESPUESTA_OPERACION");
        i.putExtra("datosJson",result);

        sendBroadcast(i);
    }

    private String POST (String uri, JSONObject datosJson){
        HttpURLConnection urlConnection = null;
        String result ="";
        try
        {

            URL mUrl = new URL(uri);

            urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(datosJson.toString().getBytes("UTF-8"));

            wr.flush();

            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED))
            {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();

            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
            } else {
                result = "NO_OK";
            }

            mException = null;
            wr.close();
            urlConnection.disconnect();
            return result;

        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private StringBuilder convertInputStreamToString(InputStreamReader inputStream) throws IOException {
        BufferedReader br = new BufferedReader(inputStream);
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null){
            result.append(line + "\n");
        }
        br.close();
        return result;
    }

}