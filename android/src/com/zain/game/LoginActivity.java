package com.zain.game;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.WindowManager;


public class LoginActivity extends AppCompatActivity {

    private ImageView imageView;
    private Handler handler;
    private boolean isFirstImageVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.imageView);
        handler = new Handler();
        toggleImageRunnable.run();
    }

    private final Runnable toggleImageRunnable = new Runnable() {
        @Override
        public void run() {
            if (isFirstImageVisible) {
                imageView.setImageResource(R.drawable.bird);
            } else {
                imageView.setImageResource(R.drawable.bird2);
            }
            isFirstImageVisible = !isFirstImageVisible;
            handler.postDelayed(this, 100);
        }
    };

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
        }

        startActivity(intent);
    }

    public void leaderboard(View view){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }
}