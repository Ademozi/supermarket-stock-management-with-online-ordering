package com.example.finalappli11;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnGoToSignUp);

        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> {
                startActivity(new Intent(this, HomeActivity.class));
            });
        }

        if (btnSignUp != null) {
            btnSignUp.setOnClickListener(v -> {
                startActivity(new Intent(this, SignUpActivity.class));
            });
        }
    }
}
