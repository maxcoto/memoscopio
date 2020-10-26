package com.example.memoscopio;

import android.os.AsyncTask;
import android.os.Handler;
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

public class RefreshToken extends AsyncTask<String, String, String> {

    private final static int TOKEN_TIMEOUT = 1500000;

    public RefreshToken(){
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {

        boolean serverResponding = true;

        while(serverResponding){

            try {
                Thread.sleep(TOKEN_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String response = doRefresh();
            JSONObject json;
            try {
                json = new JSONObject(response);
                String success = json.getString("success");
                if(success.equals("true")) {
                    User.token = json.getString("token");
                    User.token_refresh = json.getString("token_refresh");
                } else {
                    serverResponding = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                serverResponding = false;
            }
        }

        return "";
    }

    private String doRefresh() {
        HttpURLConnection connection;
        String result;

        try {
            URL url = new URL(Constants.REFRESH_URI);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Authorization", "Bearer " + User.token_refresh);
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.setRequestMethod("PUT");

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write("".getBytes("UTF-8"));
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

            Log.e("LOGUEO REFRESH","MSG: \n" + result);

            out.close();
            connection.disconnect();
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
        } catch (JSONException e) {
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
