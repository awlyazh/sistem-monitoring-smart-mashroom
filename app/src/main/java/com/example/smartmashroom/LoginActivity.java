package com.example.smartmashroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.security.GeneralSecurityException;

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

        // ✅ Sembunyikan ActionBar atas
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.etEmail);
        passwordInput = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
        lupaSandi = findViewById(R.id.tvForgotPassword);
        passwordToggle = findViewById(R.id.ivPasswordToggle);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        mAuth = FirebaseAuth.getInstance();

        try {
            // ✅ Gunakan EncryptedSharedPreferences
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    this,
                    "secure_login_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            editor = sharedPreferences.edit();

            if (sharedPreferences.getBoolean("rememberMe", false)) {
                String savedEmail = sharedPreferences.getString("email", "");
                String savedPassword = sharedPreferences.getString("password", "");
                emailInput.setText(savedEmail);
                passwordInput.setText(savedPassword);
                cbRememberMe.setChecked(true);
            }

        } catch (GeneralSecurityException | IOException e) {
            Toast.makeText(this, "Gagal inisialisasi keamanan", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        loginBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email dan sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

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
        });

        lupaSandi.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
        });

        passwordToggle.setOnClickListener(v -> {
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
        });
    }
}
