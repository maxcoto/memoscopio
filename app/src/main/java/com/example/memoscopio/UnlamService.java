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

    public static final String ACTION_REGISTER = "com.example.memoscopio.action.REGISTER";
    public static final String ACTION_LOGIN = "com.example.memoscopio.action.LOGIN";
    public static final String ACTION_EVENT = "com.example.memoscopio.action.EVENT";

    private Exception exception = null;

    public UnlamService() {
        super("UnlamService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            String uri = intent.getStringExtra("uri");
            String action = intent.getStringExtra("action");
            String data = intent.getStringExtra("data");

            JSONObject json = new JSONObject(data);
            json.put("env", Constants.ENVIRONMENT);
            data = json.toString();

            executePost(uri, action, data);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void executePost(String uri, String action, String data){
        String result = POST(uri, data);

        if(result == null){
            Log.e("LOGUEO_SERVICE","Error en Get: \n" + exception.toString());
            return;
        }

        if (result == "NO_OK"){
            Log.e("LOGUEO_SERVICE","SE RECIBIO RESPONSE NO_OK");
            return;
        }

        Intent i = new Intent(action);
        i.putExtra("data", result);
        sendBroadcast(i);
    }

    private String POST (String uri, String data){
        HttpURLConnection urlConnection = null;
        String result ="";

        try {
            URL url = new URL(uri);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestMethod("POST");

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(data.getBytes("UTF-8"));
            wr.flush();
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();

            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();
            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStream = new InputStreamReader(urlConnection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
            } else {
                result = "NO_OK";
            }

            exception = null;
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