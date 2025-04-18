package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditPasswordActivity extends AppCompatActivity {

    EditText etOldPassword, etNewPassword, etConfirmPassword;
    Button btnSimpan;
    CheckBox cbLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        cbLogout = findViewById(R.id.cb_logout);
        btnSimpan = findViewById(R.id.btn_simpan_password);

        btnSimpan.setOnClickListener(v -> {
            String oldPass = etOldPassword.getText().toString();
            String newPass = etNewPassword.getText().toString();
            String confirmPass = etConfirmPassword.getText().toString();

            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Konfirmasi sandi tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPass.length() < 6) {
                Toast.makeText(this, "Kata sandi minimal 6 karakter", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Kata sandi berhasil diubah", Toast.LENGTH_SHORT).show();
            finish();
        });

        // ==== Bottom Navigation ====
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_profile); // aktifkan tab Profil

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_home) {
                startActivity(new Intent(EditPasswordActivity.this, BerandaActivity.class));
                return true;
            } else if (id == R.id.menu_info) {
                startActivity(new Intent(EditPasswordActivity.this, InformasiActivity.class));
                return true;
            } else if (id == R.id.menu_fogging) {
                startActivity(new Intent(EditPasswordActivity.this, PengembunanActivity.class));
                return true;
            } else if (id == R.id.menu_notif) {
                startActivity(new Intent(EditPasswordActivity.this, NotifikasiActivity.class));
                return true;
            } else if (id == R.id.menu_profile) {
                startActivity(new Intent(EditPasswordActivity.this, ProfilActivity.class));
                return true;
            }
            return false;
        });
    }
}
