package com.example.smartmashroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginBtn;
    private TextView lupaSandi;
    private ImageView passwordToggle;
    private CheckBox cbRememberMe;
    private boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
        lupaSandi = findViewById(R.id.tvForgotPassword);
        passwordToggle = findViewById(R.id.ivPasswordToggle);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Cek apakah rememberMe dicentang sebelumnya
        if (sharedPreferences.getBoolean("rememberMe", false)) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");

            emailInput.setText(savedEmail);
            passwordInput.setText(savedPassword);
            cbRememberMe.setChecked(true);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email dan sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Simpan email dan password jika checkbox dicentang
                if (cbRememberMe.isChecked()) {
                    editor.putBoolean("rememberMe", true);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();
                } else {
                    editor.clear();
                    editor.apply();
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login berhasil", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, BerandaActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Email atau password salah", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        lupaSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        passwordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.ic_eye_closed);
                    isPasswordVisible = false;
                } else {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordToggle.setImageResource(R.drawable.ic_eye_open);
                    isPasswordVisible = true;
                }
                passwordInput.setSelection(passwordInput.getText().length());
            }
        });
    }
}
