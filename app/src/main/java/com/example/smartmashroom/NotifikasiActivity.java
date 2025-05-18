package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotifikasiActivity extends AppCompatActivity {

    private LinearLayout notificationContainer;
    private ImageButton btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        notificationContainer = findViewById(R.id.notification_container);
        btnDelete = findViewById(R.id.btn_delete);

        // Tombol hapus notifikasi yang dipilih
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletedCount = 0;
                for (int i = notificationContainer.getChildCount() - 1; i >= 0; i--) {
                    View notifView = notificationContainer.getChildAt(i);
                    if (notifView instanceof LinearLayout) {
                        LinearLayout notifLayout = (LinearLayout) notifView;
                        if (notifLayout.getChildCount() > 0 && notifLayout.getChildAt(0) instanceof CheckBox) {
                            CheckBox checkBox = (CheckBox) notifLayout.getChildAt(0);
                            if (checkBox.isChecked()) {
                                notificationContainer.removeViewAt(i);
                                deletedCount++;
                            }
                        }
                    }
                }
                if (deletedCount > 0) {
                    Toast.makeText(NotifikasiActivity.this, deletedCount + " notifikasi dihapus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NotifikasiActivity.this, "Tidak ada notifikasi yang dipilih", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_notif); // Item yang aktif saat ini

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    startActivity(new Intent(NotifikasiActivity.this, BerandaActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.menu_fogging) {
                    startActivity(new Intent(NotifikasiActivity.this, PengembunanActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (id == R.id.menu_notif) {
                    return true; // Sudah di halaman notifikasi
                } else if (id == R.id.menu_profile) {
                    startActivity(new Intent(NotifikasiActivity.this, ProfilActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }

                return false;
            }
        });
    }
}
