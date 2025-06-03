package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LihatEmailActivity extends AppCompatActivity {

    TextView tvEmailLama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email); // jika sudah ganti nama XML, ganti juga di sini

        tvEmailLama = findViewById(R.id.tv_email_lama);

        // Ambil email dari Firebase (atau bisa juga dari Intent jika dikirim dari activity sebelumnya)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            tvEmailLama.setText(user.getEmail());
        } else {
            tvEmailLama.setText("Email tidak tersedia");
        }

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
