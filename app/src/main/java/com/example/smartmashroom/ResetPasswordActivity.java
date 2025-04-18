package com.example.smartmashroom;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password); // Pastikan layout ini ada dan sesuai

        // Inisialisasi input dan tombol
        EditText etNewPassword = findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnSubmit = findViewById(R.id.btnSubmitNewPassword);

        // Aksi ketika tombol diklik
        btnSubmit.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(ResetPasswordActivity.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ResetPasswordActivity.this, "Sandi tidak cocok", Toast.LENGTH_SHORT).show();
            } else {
                // Logika update password (simulasi saja)
                Toast.makeText(ResetPasswordActivity.this, "Sandi berhasil diubah", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke login
            }
        });
    }
}
