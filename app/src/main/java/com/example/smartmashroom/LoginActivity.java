package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView lupaSandi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi komponen
        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
<<<<<<< HEAD
        lupaSandi = findViewById(R.id.tvLupaSandi); // ID harus sesuai dengan XML
=======
        lupaSandi = findViewById(R.id.tvForgotPassword); // ✅ ID disesuaikan dengan XML
>>>>>>> 543f8b392e507ba6660bcfaf1cc0869d1a236be8

        // Aksi tombol Login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Validasi input kosong
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email dan sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi login statis
                if (email.equalsIgnoreCase("suardi@gmail.com") && password.equals("123456")) {
                    Intent intent = new Intent(LoginActivity.this, BerandaActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Aksi klik "Lupa sandi"
        lupaSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
