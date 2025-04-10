package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditNamaActivity extends AppCompatActivity {

    EditText editNama;
    Button btnSimpan;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nama);

        editNama = findViewById(R.id.edit_nama);
        btnSimpan = findViewById(R.id.btn_simpan);
        tvName = findViewById(R.id.tv_name);

        Intent intent = getIntent();
        String namaSekarang = intent.getStringExtra("nama");

        if (namaSekarang != null) {
            editNama.setText(namaSekarang);
            tvName.setText(namaSekarang);
        }

        btnSimpan.setOnClickListener(v -> {
            String namaBaru = editNama.getText().toString();
            Intent resultIntent = new Intent();
            resultIntent.putExtra("namaBaru", namaBaru);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // ===== BOTTOM NAVIGATION =====
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_profile); // set item aktif

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                startActivity(new Intent(EditNamaActivity.this, BerandaActivity.class));
                return true;
            } else if (id == R.id.menu_info) {
                startActivity(new Intent(EditNamaActivity.this, InformasiActivity.class));
                return true;
            } else if (id == R.id.menu_fogging) {
                startActivity(new Intent(EditNamaActivity.this, PengembunanActivity.class));
                return true;
            } else if (id == R.id.menu_notif) {
                startActivity(new Intent(EditNamaActivity.this, NotifikasiActivity.class));
                return true;
            } else if (id == R.id.menu_profile) {
                startActivity(new Intent(EditNamaActivity.this, ProfilActivity.class));
                return true;
            }
            return false;
        });
    }
}
