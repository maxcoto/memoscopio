package com.example.memoscopio;

import android.app.IntentService;
import android.content.Intent;
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

    public static final int TIMEOUT = 5000;
    public static final String ACTION_REGISTER = "com.example.memoscopio.action.REGISTER";
    public static final String ACTION_LOGIN = "com.example.memoscopio.action.LOGIN";
    public static final String ACTION_EVENT = "com.example.memoscopio.action.EVENT";

    public static final String ACTION_RANKING_GET = "com.example.memoscopio.action.RANKING_GET";

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
            String method = intent.getStringExtra("method");

            JSONObject json = new JSONObject(data);
            json.put("env", Constants.ENVIRONMENT);
            data = json.toString();

            executeRequest(uri, action, data, method);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void executeRequest(String uri, String action, String data, String method){
        String result = REQUEST(uri, data, method);

        if(result == null){
            exception.printStackTrace();
            Log.e("LOGUEO_SERVICE","Exception found, stacktrace: \n" + exception.toString());
            return;
        }

        Log.i("LOGUEO ACTION", action);
        Log.i("LOGUEO URI", uri);
        Log.i("LOGUEO DATA", data);

        Intent i = new Intent(action);
        i.putExtra("data", result);
        sendBroadcast(i);
    }

    private String REQUEST (String uri, String data, String method){
        HttpURLConnection connection;
        String result;

        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();
            if(User.token.length() > 0) {
                connection.setRequestProperty("Authorization", "Bearer " + User.token);
            }
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestMethod(method);
            DataOutputStream out = null;

            if(method.equals("POST")) {
                connection.setDoOutput(true);
                connection.setDoInput(true);
                out = new DataOutputStream(connection.getOutputStream());
                out.write(data.getBytes("UTF-8"));
                out.flush();
            }

            connection.connect();

            int responseCode = connection.getResponseCode();

            Log.i("LOGUEO REQUEST","RESPONSE CODE " + responseCode);
            Log.i("LOGUEO_REQUEST","METHOD " + method);

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

            if(out != null) out.close();
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