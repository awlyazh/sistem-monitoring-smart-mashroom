package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditEmailActivity extends AppCompatActivity {

    EditText etEmailLama, etEmailBaru;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);

        etEmailLama = findViewById(R.id.et_email_lama);
        etEmailBaru = findViewById(R.id.et_email_baru);
        btnSimpan = findViewById(R.id.btn_simpan_email);

        // Set email lama dari intent
        String emailSekarang = getIntent().getStringExtra("email");
        etEmailLama.setText(emailSekarang);
        etEmailLama.setEnabled(false); // agar tidak bisa diedit

        // Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String emailBaru = etEmailBaru.getText().toString().trim();

            if (emailBaru.isEmpty()) {
                etEmailBaru.setError("Email tidak boleh kosong");
                etEmailBaru.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailBaru).matches()) {
                etEmailBaru.setError("Format email tidak valid");
                etEmailBaru.requestFocus();
                return;
            }

            // Return ke activity sebelumnya
            Intent resultIntent = new Intent();
            resultIntent.putExtra("emailBaru", emailBaru);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_profile);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(this, BerandaActivity.class));
                return true;
            } else if (itemId == R.id.menu_fogging) {
                startActivity(new Intent(this, PengembunanActivity.class));
                return true;
            } else if (itemId == R.id.menu_notif) {
                startActivity(new Intent(this, NotifikasiActivity.class));
                return true;
            } else if (itemId == R.id.menu_profile) {
                startActivity(new Intent(this, ProfilActivity.class));
                return true;
            }
            return false;
        });
    }
}
