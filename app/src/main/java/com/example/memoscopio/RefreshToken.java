package com.example.memoscopio;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
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

    public RefreshToken(){
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {

        new CountDownTimer(5000, 3000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                String response = doRefresh();
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    User.token = json.getString("token");
                    User.token_refresh = json.getString("token_refresh");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new RefreshToken().execute();
            }
        }.start();

        return "";
    }

    private String doRefresh() {
        HttpURLConnection connection = null;
        String result ="";

        try {
            URL url = new URL(Constants.REFRESH_URI);
            connection = (HttpURLConnection) url.openConnection();
            Log.e("LOGUEO REFRESH","MSG: 0 \n");
            if(User.token_refresh.length() > 0){
                connection.setRequestProperty("Authorization", "Bearer " + User.token_refresh);
            } else {
                connection.setRequestProperty("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDM2MDExNDQsInR5cGUiOiJyZWZyZXNoIiwidXNlciI6eyJlbWFpbCI6InRlc3RAdGVzdC5jb20iLCJkbmkiOjEyMzQ1Njc4fX0.55USFLf5iI9guVceeCy_-62lSUv0t-fQsbF6i6_pcn8");
            }
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.setRequestMethod("PUT");

            Log.e("LOGUEO REFRESH","MSG: 1 \n");

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write("".getBytes("UTF-8"));
            out.flush();

            connection.connect();

            Log.e("LOGUEO REFRESH","MSG: 2 \n");

            int responseCode = connection.getResponseCode();

            if((responseCode == HttpURLConnection.HTTP_OK) || (responseCode == HttpURLConnection.HTTP_CREATED)) {
                InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
                result = convertInputStreamToString(inputStream).toString();
            } else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStreamReader inputStream = new InputStreamReader(connection.getErrorStream());
                result = convertInputStreamToString(inputStream).toString();
            } else {
                result = "NO_OK";
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
