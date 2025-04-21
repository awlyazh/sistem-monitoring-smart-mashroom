<<<<<<< HEAD
// BerandaActivity.java
package com.example.smartmashroom;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BerandaActivity extends AppCompatActivity {
=======
package com.example.smartmashroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BerandaActivity extends AppCompatActivity {

>>>>>>> 543f8b392e507ba6660bcfaf1cc0869d1a236be8
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
<<<<<<< HEAD
    }
}
=======

        // Inisialisasi BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Listener klik item navigasi bawah
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_profile) {
                    // Buka halaman profil
                    Intent intent = new Intent(BerandaActivity.this, ProfilActivity.class);
                    startActivity(intent);
                    return true;
                }

                // Tambahkan menu lain jika perlu
                return false;
            }
        });
    }
}
>>>>>>> 543f8b392e507ba6660bcfaf1cc0869d1a236be8
