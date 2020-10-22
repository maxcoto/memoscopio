package com.example.memoscopio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button registerButton;
    private Button loginButton;

    private TextView emailInput;
    private TextView passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        Connection.check(LoginActivity.this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Connection.check(LoginActivity.this)) {
                    if(validate()){
                        error("todo ok");
                        //Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        //startActivity(intent);
                    }
                }

            }
        });
    }

    private boolean validate(){
        String text;

        text = emailInput.getText().toString().trim();
        if(text.length() == 0) {
            error("El email no puede estar en blanco");
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(!text.matches(emailPattern)){
            error("Formato de email invalido");
            return false;
        }

        text = passwordInput.getText().toString().trim();
        if(text.length() < 8) {
            error("Debe ingresar un password valido");
            return false;
        }

        return true;
    }

    private void error(String texto) {
        Toast.makeText(LoginActivity.this, texto, Toast.LENGTH_SHORT).show();
    }
}