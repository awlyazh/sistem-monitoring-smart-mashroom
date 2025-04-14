package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditEmailActivity extends AppCompatActivity {

    EditText etEmailBaru;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        etEmailBaru = findViewById(R.id.et_email_baru);
        btnSimpan = findViewById(R.id.btn_simpan_email);

        // Ambil email dari intent
        String emailSekarang = getIntent().getStringExtra("email");
        etEmailBaru.setText(emailSekarang);

        // Aksi simpan
        btnSimpan.setOnClickListener(v -> {
            String emailBaru = etEmailBaru.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("emailBaru", emailBaru);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_profile); // Set item aktif

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.menu_home) {
                startActivity(new Intent(EditEmailActivity.this, BerandaActivity.class));
                return true;
            } else if (itemId == R.id.menu_info) {
                startActivity(new Intent(EditEmailActivity.this, InformasiActivity.class));
                return true;
            } else if (itemId == R.id.menu_fogging) {
                startActivity(new Intent(EditEmailActivity.this, PengembunanActivity.class));
                return true;
            } else if (itemId == R.id.menu_notif) {
                startActivity(new Intent(EditEmailActivity.this, NotifikasiActivity.class));
                return true;
            } else if (itemId == R.id.menu_profile) {
                startActivity(new Intent(EditEmailActivity.this, ProfilActivity.class));
                return true;
            }
            return false;
        });
    }
}
