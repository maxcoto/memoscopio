package com.example.memoscopio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;


public class RegisterActivity extends AppCompatActivity implements UnlamCallback {

    private TextView nombreInput;
    private TextView apellidoInput;
    private TextView dniInput;
    private TextView emailInput;
    private TextView passwordInput;
    private TextView comisionInput;

    private Button loginButton;
    private Button registerButton;

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
                        //Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        //startActivity(intent);
                    }
                }
            }
        });


        new UnlamAPI(this).execute();

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

        text = nombreInput.getText().toString().trim();
        if(text.length() == 0) {
            error("El nombre no puede estar en blanco");
            return false;
        }

        text = apellidoInput.getText().toString().trim();
        if(text.length() == 0) {
            error("El apellido no puede estar en blanco");
            return false;
        }

        text = dniInput.getText().toString().trim();
        if(text.length() == 0) {
            error("El DNI no puede estar en blanco");
            return false;
        }

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
            error("La longitud del password debe ser de 8 caracteres como minimo");
            return false;
        }

        text = comisionInput.getText().toString().trim();
        if(text.length() == 0) {
            error("La comision no puede estar en blanco");
            return false;
        }

        return true;
    }

    private void error(String texto) {
        Toast.makeText(RegisterActivity.this, texto, Toast.LENGTH_LONG).show();
    }
}