package com.zain.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view){
        Intent intent = new Intent(this, AndroidLauncher.class);
        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        intent.putExtra("username", username);

        if (!password.equals("hello")) {
            System.out.println(password);
            Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(intent);
    }
}