package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotifikasiActivity extends AppCompatActivity {

    private LinearLayout notifikasiContainer;
    private LinearLayout notifikasi1;
    private ImageView btnHapus1;
    private Button btnHapusSemua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        // Inisialisasi komponen tampilan
        notifikasiContainer = findViewById(R.id.notifikasiContainer);
        notifikasi1 = findViewById(R.id.notifikasi1);
        btnHapus1 = findViewById(R.id.btnHapus1);
        btnHapusSemua = findViewById(R.id.btnHapusSemua);

        // Aksi untuk menghapus notifikasi satu per satu
        btnHapus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifikasi1.setVisibility(View.GONE);
            }
        });

        // Aksi untuk menghapus semua notifikasi
        btnHapusSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifikasiContainer.removeAllViews();
            }
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_notif);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    startActivity(new Intent(getApplicationContext(), BerandaActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.menu_fogging) {
                    startActivity(new Intent(getApplicationContext(), PengembunanActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.menu_notif) {
                    return true;
                } else if (id == R.id.menu_profile) {
                    startActivity(new Intent(getApplicationContext(), ProfilActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });
    }
}
