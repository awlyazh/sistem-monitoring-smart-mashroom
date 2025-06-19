package com.example.smartmashroom;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotifikasiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotifikasiAdapter adapter;
    private List<NotifikasiItem> notifList;
    private ImageButton btnDelete;
    private FirebaseFirestore db;

    private static final long SIX_HOURS_MILLIS = 6 * 60 * 60 * 1000;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;

    private Double lastSuhu = null;
    private Double lastKelembaban = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        recyclerView = findViewById(R.id.recyclerViewNotifikasi);
        btnDelete = findViewById(R.id.btn_delete);
        db = FirebaseFirestore.getInstance();

        notifList = new ArrayList<>();
        adapter = new NotifikasiAdapter(notifList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        checkNotificationPermission();
        loadNotifications();

        btnDelete.setOnClickListener(v -> {
            adapter.setShowCheckboxes(true); // Tampilkan checkbox untuk memilih

            List<NotifikasiItem> selected = new ArrayList<>();
            for (NotifikasiItem item : notifList) {
                if (item.isSelected()) selected.add(item);
            }

            if (selected.isEmpty()) {
                Toast.makeText(this, "Tidak ada notifikasi yang dipilih", Toast.LENGTH_SHORT).show();
                return;
            }

            for (NotifikasiItem item : selected) {
                db.collection("notifikasi")
                        .whereEqualTo("status", item.getStatus())
                        .whereEqualTo("tanggal", item.getTanggal())
                        .get()
                        .addOnSuccessListener(snapshot -> {
                            for (QueryDocumentSnapshot doc : snapshot) {
                                doc.getReference().delete();
                            }
                            notifList.removeAll(selected);
                            adapter.setShowCheckboxes(false); // Sembunyikan kembali checkbox
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, selected.size() + " notifikasi dihapus", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Gagal menghapus notifikasi", Toast.LENGTH_SHORT).show()
                        );
            }
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_notif);

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_home) {
                startActivity(new Intent(this, BerandaActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.menu_fogging) {
                startActivity(new Intent(this, PengembunanActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.menu_notif) {
                return true;
            } else if (id == R.id.menu_profile) {
                startActivity(new Intent(this, ProfilActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

        db.collection("sensor_data").document("latest")
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null || snapshot == null || !snapshot.exists()) return;

                    Double suhu = snapshot.getDouble("suhu");
                    Double kelembaban = snapshot.getDouble("kelembaban");

                    if (suhu != null && kelembaban != null) {
                        cekSuhuKelembaban(suhu, kelembaban);
                    }
                });
    }

    private void loadNotifications() {
        db.collection("notifikasi")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    notifList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String status = doc.getString("status");
                        Timestamp tanggal = doc.getTimestamp("tanggal");
                        notifList.add(new NotifikasiItem(status, tanggal));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Gagal mengambil notifikasi", e);
                    Toast.makeText(this, "Gagal mengambil notifikasi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public void addNotification(String status, Timestamp currentTimestamp) {
        long now = System.currentTimeMillis();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        long lastSavedTimestamp = prefs.getLong("lastSavedTimestamp", 0);

        notifList.add(0, new NotifikasiItem(status, currentTimestamp));
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);

        if (now - lastSavedTimestamp > SIX_HOURS_MILLIS) {
            saveNotificationToFirestore(status, currentTimestamp);
            prefs.edit().putLong("lastSavedTimestamp", now).apply();
        }
    }

    private void saveNotificationToFirestore(String status, Timestamp timestamp) {
        db.collection("notifikasi")
                .add(new NotifikasiItem(status, timestamp))
                .addOnSuccessListener(docRef ->
                        Toast.makeText(this, "Notifikasi disimpan", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Gagal menyimpan notifikasi", Toast.LENGTH_SHORT).show());
    }

    private void cekSuhuKelembaban(double suhu, double kelembaban) {
        if (lastSuhu != null && lastKelembaban != null) {
            if (Math.abs(suhu - lastSuhu) < 0.5 && Math.abs(kelembaban - lastKelembaban) < 1) {
                return;
            }
        }

        lastSuhu = suhu;
        lastKelembaban = kelembaban;

        List<String> masalah = new ArrayList<>();

        if (suhu < 24) {
            masalah.add("Suhu terlalu rendah");
        } else if (suhu > 27) {
            masalah.add("Suhu terlalu tinggi");
        }

        if (kelembaban < 80) {
            masalah.add("Kelembaban terlalu rendah");
        } else if (kelembaban > 90) {
            masalah.add("Kelembaban terlalu tinggi");
        }

        if (!masalah.isEmpty()) {
            String status = String.join(" dan ", masalah);
            Timestamp now = Timestamp.now();
            addNotification(status, now);
            NotifikasiHelper.showNotification(this, status);
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION_PERMISSION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Izin notifikasi diberikan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Izin notifikasi ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
