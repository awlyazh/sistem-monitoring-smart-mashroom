package com.example.smartmashroom;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PengembunanActivity extends AppCompatActivity {

    private Switch switchAuto;
    private Button btnManual;
    private TextView textStatus;
    private CardView cardHumidity;
    private CardView cardTemp;

    private DatabaseReference mDatabase;

    private boolean isManualOn = false;
    private float currentSuhu = 0;
    private float currentKelembapan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengembunan);

        switchAuto = findViewById(R.id.switchAuto);
        btnManual = findViewById(R.id.btnManual);
        textStatus = findViewById(R.id.textStatus);
        cardHumidity = findViewById(R.id.cardHumidity);
        cardTemp = findViewById(R.id.cardTemp);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        switchAuto.setOnCheckedChangeListener((buttonView, isChecked) -> updateMode(isChecked));
        btnManual.setOnClickListener(v -> toggleManualMode());

        monitorSensorData();
    }

    private void monitorSensorData() {
        mDatabase.child("sensor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double suhu = snapshot.child("suhu").getValue(Double.class);
                    Double kelembapan = snapshot.child("kelembapan").getValue(Double.class);

                    if (suhu != null) currentSuhu = suhu.floatValue();
                    if (kelembapan != null) currentKelembapan = kelembapan.floatValue();

                    Log.d("Sensor", "Suhu: " + currentSuhu + " | Kelembapan: " + currentKelembapan);

                    if (switchAuto.isChecked()) {
                        updateAutoControl();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                textStatus.setText("Gagal membaca data sensor.");
            }
        });
    }

    private void updateMode(boolean isAutomatic) {
        if (isAutomatic) {
            mDatabase.child("pompa").child("mode_pompa").setValue("otomatis");
            btnManual.setEnabled(false);
            updateAutoControl();
        } else {
            mDatabase.child("pompa").child("mode_pompa").setValue("manual");
            btnManual.setEnabled(true);
            textStatus.setText("Mode Manual: Atur Sendiri");
            updateManualUI();
        }
    }

    private void updateAutoControl() {
        if (currentSuhu < 24 || currentSuhu > 27 || currentKelembapan < 80 || currentKelembapan > 90) {
            // Tidak stabil
            mDatabase.child("status_pompa").setValue("ON");
            textStatus.setText("Pengembunan Aktif (Otomatis)");
            cardHumidity.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_humid_unstable));
            cardTemp.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_temp_unstable));
        } else {
            // Stabil
            mDatabase.child("status_pompa").setValue("OFF");
            textStatus.setText("Kondisi Stabil, Pengembunan OFF");
            cardHumidity.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_humid_stable));
            cardTemp.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_temp_stable));
        }
    }

    private void toggleManualMode() {
        isManualOn = !isManualOn;
        mDatabase.child("status_pompa").setValue(isManualOn ? "ON" : "OFF");
        updateManualUI();
    }

    private void updateManualUI() {
        if (isManualOn) {
            textStatus.setText("Pengembunan Manual: Aktif");
            btnManual.setBackgroundColor(ContextCompat.getColor(this, R.color.button_on));
            cardHumidity.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_humid_stable));
            cardTemp.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_temp_stable));
        } else {
            textStatus.setText("Pengembunan Manual: OFF");
            btnManual.setBackgroundColor(ContextCompat.getColor(this, R.color.button_off));
            cardHumidity.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_off));
            cardTemp.setCardBackgroundColor(ContextCompat.getColor(this, R.color.card_off));
        }
    }
}
