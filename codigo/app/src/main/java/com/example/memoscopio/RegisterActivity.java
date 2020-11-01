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


public class RegisterActivity extends AppCompatActivity {

    private TextView nombreInput;
    private TextView apellidoInput;
    private TextView dniInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView comisionInput;

    public IntentFilter filter;
    private final Callback callback = new Callback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nombreInput     = findViewById(R.id.nombreInput);
        apellidoInput   = findViewById(R.id.apellidoInput);
        dniInput        = findViewById(R.id.dniInput);
        emailInput      = findViewById(R.id.emailInput);
        passwordInput   = findViewById(R.id.passwordInput);
        comisionInput   = findViewById(R.id.comisionInput);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(loginHandler);
        registerButton.setOnClickListener(registerHandler);

        Connection.check(RegisterActivity.this);
        configureReceiver();
    }

    private final View.OnClickListener loginHandler = (_v) -> {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    };

    // envia los datos al servidor solo si hay internet
    // y si los datos del usuario son validos para el registro
    private final View.OnClickListener registerHandler = (_v) -> {
        User user = new User(getValue(nombreInput), getValue(apellidoInput), getValue(dniInput), getValue(emailInput), getValue(passwordInput), getValue(comisionInput));

        if(Connection.check(RegisterActivity.this)){
            String result = user.validateRegister();

            if(result.equals("ok")){
                Intent intent = new Intent(RegisterActivity.this, UnlamService.class);
                intent.putExtra("uri", Constants.REGISTER_URI);
                intent.putExtra("action", UnlamService.ACTION_REGISTER);
                intent.putExtra("data", user.registerData());
                intent.putExtra("method", "POST");
                startService(intent);
            } else {
                error(result);
            }
        }
    };

    private String getValue(TextView input){
        return input.getText().toString().trim();
    }

    // registra el receptor de IntentService como callback
    private void configureReceiver(){
        filter = new IntentFilter(UnlamService.ACTION_REGISTER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(callback, filter);
    }

    // recibe la respuesta del servidor
    // en caso de exito asigna el token y refresh del usuario y pone en ejecucion el refresh token
    // luego procede a la vista del menu
    // en caso de error muestra el mensaje en pantalla
    public class Callback extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String data = intent.getStringExtra("data");
                JSONObject json = new JSONObject(data);
                Log.i("LOGUEO REGISTER", "Datos: " + data );
                String success = json.getString("success");

                if(success.equals("true")){
                    User.token = json.getString("token");
                    User.token_refresh = json.getString("token_refresh");
                    error("Registrado correctamente");

                    new RefreshToken().execute();
                    Intent i = new Intent(context, MenuActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    String err =  json.getString("msg");
                    error(err);
                }

            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private void error(String texto) {
        Toast.makeText(RegisterActivity.this, texto, Toast.LENGTH_LONG).show();
    }
}