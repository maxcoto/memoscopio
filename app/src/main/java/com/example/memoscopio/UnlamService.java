package com.example.memoscopio;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
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
            exception.printStackTrace();
            Log.e("LOGUEO_SERVICE","Exception found, stacktrace: \n" + exception.toString());
            return;
        }

        Intent i = new Intent(action);
        i.putExtra("data", result);
        sendBroadcast(i);
    }

    private String POST (String uri, String data){
        HttpURLConnection connection = null;
        String result ="";

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            if(User.token.length() > 0) {
                connection.setRequestProperty("Authorization", "Bearer " + User.token);
            }
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.setRequestMethod("POST");

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(data.getBytes("UTF-8"));
            out.flush();
            connection.connect();

            int responseCode = connection.getResponseCode();

            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)) {
                InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();
            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStream = new InputStreamReader(connection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
            } else {
                JSONObject error = new JSONObject();
                error.put("success", "false");
                error.put("msg", "Error en el servidor");
                result = error.toString();
            }

            out.close();
            connection.disconnect();
            return result;

        } catch (ProtocolException e) {
            exception = e;
            return null;
        } catch (MalformedURLException e) {
            exception = e;
            return null;
        } catch (IOException e) {
            exception = e;
            return null;
        } catch (JSONException e){
            exception = e;
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