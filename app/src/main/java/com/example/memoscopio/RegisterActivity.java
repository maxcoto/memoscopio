package com.example.memoscopio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;


public class RegisterActivity extends AppCompatActivity {

    private TextView nombreInput;
    private TextView apellidoInput;
    private TextView dniInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView comisionInput;

    private Button loginButton;
    private Button registerButton;

    private JSONObject data = new JSONObject();

    public IntentFilter filtro;
    private ReceptorOperacion receiver = new ReceptorOperacion();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombreInput = findViewById(R.id.nombreInput);
        apellidoInput = findViewById(R.id.apellidoInput);
        dniInput = findViewById(R.id.dniInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        comisionInput = findViewById(R.id.comisionInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        Connection.check(RegisterActivity.this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Connection.check(RegisterActivity.this)){
                    if(validate()){
                        error("todo ok");
                        Intent intent = new Intent(RegisterActivity.this, UnlamService.class);
                        intent.putExtra("uri", "http://so-unlam.net.ar/api/api/register");
                        intent.putExtra("datosJson", data.toString());
                        startService(intent);
                    }
                }
            }
        });

        configurarBroadcastReceiver();

        try {
            data.put("env", "TEST");
            data.put("name", "maxi");
            data.put("lastname", "perez");
            data.put("dni", "35360791");
            data.put("email", "maxi@maxi.com");
            data.put("password", "12345678");
            data.put("commission", "123");
        } catch (Exception e){
            e.printStackTrace();
        }

        Intent intent = new Intent(RegisterActivity.this, UnlamService.class);
        intent.putExtra("uri", "http://so-unlam.net.ar/api/api/register");
        intent.putExtra("datosJson", data.toString());
        startService(intent);

    }

    private void configurarBroadcastReceiver(){
        filtro = new IntentFilter("com.example.memoscopio.action.RESPUESTA_OPERACION");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filtro);
    }


    public class ReceptorOperacion extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String datosJsonString = intent.getStringExtra("datosJson");
                JSONObject datosJson = new JSONObject(datosJsonString);

                Log.i("LOGUEO MAIN", "Datos: " + datosJson );

                String token = datosJson.getString("token");
                String token_refresh = datosJson.getString("token_refresh");

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void onUnlamCallback(String texto){
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    error(texto);
                }
            });
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean validate(){
        String text;

        try {
            data.put("env", "TEST");

            text = nombreInput.getText().toString().trim();
            if (text.length() == 0) {
                error("El nombre no puede estar en blanco");
                return false;
            }
            data.put("name", text);

            text = apellidoInput.getText().toString().trim();
            if (text.length() == 0) {
                error("El apellido no puede estar en blanco");
                return false;
            }
            data.put("lastname", text);

            text = dniInput.getText().toString().trim();
            if (text.length() == 0) {
                error("El DNI no puede estar en blanco");
                return false;
            }
            data.put("dni", Integer.parseInt(text));

            text = emailInput.getText().toString().trim();
            if (text.length() == 0) {
                error("El email no puede estar en blanco");
                return false;
            }
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if (!text.matches(emailPattern)) {
                error("Formato de email invalido");
                return false;
            }
            data.put("email", text);

            text = passwordInput.getText().toString().trim();
            if (text.length() < 8) {
                error("La longitud del password debe ser de 8 caracteres como minimo");
                return false;
            }
            data.put("password", text);

            text = comisionInput.getText().toString().trim();
            if (text.length() == 0) {
                error("La comision no puede estar en blanco");
                return false;
            }
            data.put("commission", Integer.parseInt(text));
        } catch(Exception e){
            e.printStackTrace();
        }

        return true;
    }

    private void error(String texto) {
        Toast.makeText(RegisterActivity.this, texto, Toast.LENGTH_LONG).show();
    }
}