package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilActivity extends AppCompatActivity {

    private static final int REQUEST_NAMA = 1;
    private static final int REQUEST_EMAIL = 2;
    private static final int REQUEST_PASSWORD = 3;

    LinearLayout itemNama, itemPassword, itemEmail;
    TextView tvNama, tvEmail, tvPassword;

    String nama = "Soehardi";
    String email = "jamutriamirdho@gmail.com";
    String password = "******";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        itemNama = findViewById(R.id.item_nama);
        itemPassword = findViewById(R.id.item_password);
        itemEmail = findViewById(R.id.item_email);

        tvNama = findViewById(R.id.tv_nama);
        tvEmail = findViewById(R.id.tv_email);
        tvPassword = findViewById(R.id.tv_password);

        updateUI();

        itemNama.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditNamaActivity.class);
            intent.putExtra("nama", nama);
            startActivityForResult(intent, REQUEST_NAMA);
        });

        itemPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditPasswordActivity.class);
            intent.putExtra("password", password);
            startActivityForResult(intent, REQUEST_PASSWORD);
        });

        itemEmail.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditEmailActivity.class);
            intent.putExtra("email", email);
            startActivityForResult(intent, REQUEST_EMAIL);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(this, BerandaActivity.class));
                return true;
            } else if (itemId == R.id.menu_info) {
                startActivity(new Intent(this, InformasiActivity.class));
                return true;
            } else if (itemId == R.id.menu_fogging) {
                startActivity(new Intent(this, PengembunanActivity.class));
                return true;
            } else if (itemId == R.id.menu_notif) {
                startActivity(new Intent(this, NotifikasiActivity.class));
                return true;
            } else if (itemId == R.id.menu_profile) {
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case REQUEST_NAMA:
                    nama = data.getStringExtra("namaBaru");
                    break;
                case REQUEST_EMAIL:
                    email = data.getStringExtra("emailBaru");
                    break;
                case REQUEST_PASSWORD:
                    password = data.getStringExtra("passwordBaru");
                    break;
            }
            updateUI();
        }
    }

    private void updateUI() {
        tvNama.setText(nama);
        tvEmail.setText(email);
        tvPassword.setText(password);
    }
}
