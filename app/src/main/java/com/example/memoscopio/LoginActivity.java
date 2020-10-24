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

public class LoginActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    private TextView emailInput;
    private TextView passwordInput;

    public IntentFilter filter;
    private Callback callback = new Callback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(registerHandler);
        loginButton.setOnClickListener(loginHandler);

        Connection.check(LoginActivity.this);
        configureReceiver();


        User user = new User("test@test.com", "12345678");
        Intent intent = new Intent(LoginActivity.this, UnlamService.class);
        intent.putExtra("uri", Constants.LOGIN_URI);
        intent.putExtra("action", UnlamService.ACTION_LOGIN);
        intent.putExtra("data", user.loginData());
        startService(intent);
    }

    private View.OnClickListener registerHandler = (_v) -> {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    };

    private View.OnClickListener loginHandler = (_v) -> {
        User user = new User(getValue(emailInput), getValue(passwordInput));

        if(Connection.check(LoginActivity.this)){
            String result = user.validateLogin();

            if( result == "ok"){
                Intent intent = new Intent(LoginActivity.this, UnlamService.class);
                intent.putExtra("uri", Constants.LOGIN_URI);
                intent.putExtra("action", UnlamService.ACTION_LOGIN);
                intent.putExtra("data", user.loginData());
                startService(intent);
            } else {
                error(result);
            }
        }
    };

    private void configureReceiver(){
        filter = new IntentFilter(UnlamService.ACTION_LOGIN);
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
                    User.token = json.getString("token");
                    User.token_refresh = json.getString("token_refresh");
                    error(User.token);

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

    private String getValue(TextView input){
        return input.getText().toString().trim();
    }

    private void error(String texto) {
        Toast.makeText(LoginActivity.this, texto, Toast.LENGTH_LONG).show();
    }
}