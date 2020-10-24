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

    public IntentFilter filter;
    private Callback callback = new Callback();

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
        loginButton     = findViewById(R.id.loginButton);
        registerButton  = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(loginHandler);
        registerButton.setOnClickListener(registerHandler);

        Connection.check(RegisterActivity.this);
        configureReceiver();
    }

    private View.OnClickListener loginHandler = (_v) -> {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    };

    private View.OnClickListener registerHandler = (_v) -> {
        User user = new User(getValue(nombreInput), getValue(apellidoInput), getValue(dniInput), getValue(emailInput), getValue(passwordInput), getValue(comisionInput));

        if(Connection.check(RegisterActivity.this)){
            String result = user.validateRegister();

            if( result == "ok"){
                Intent intent = new Intent(RegisterActivity.this, UnlamService.class);
                intent.putExtra("uri", Constants.REGISTER_URI);
                intent.putExtra("action", UnlamService.ACTION_REGISTER);
                intent.putExtra("data", user.registerData());
                startService(intent);
            } else {
                error(result);
            }
        }
    };

    private String getValue(TextView input){
        return input.getText().toString().trim();
    }

    private void configureReceiver(){
        filter = new IntentFilter(UnlamService.ACTION_REGISTER);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(callback, filter);
    }


    public class Callback extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String data = intent.getStringExtra("data");
                JSONObject json = new JSONObject(data);

                Log.i("LOGUEO MAIN", "Datos: " + data );

                String success = json.getString("success");

                if(success == "true"){
                    String token = json.getString("token");
                    String token_refresh = json.getString("token_refresh");
                    error(token);
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