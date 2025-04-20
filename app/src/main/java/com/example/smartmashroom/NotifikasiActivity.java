package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class NotifikasiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_notif); // Set item aktif ke menu_notif

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // Menavigasi ke halaman sesuai dengan item yang dipilih
                if (id == R.id.menu_home) {
                    startActivity(new Intent(getApplicationContext(), BerandaActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.menu_fogging) {
                    startActivity(new Intent(getApplicationContext(), PengembunanActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.menu_notif) {
                    // Halaman ini sudah aktif (Notifikasi)
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
