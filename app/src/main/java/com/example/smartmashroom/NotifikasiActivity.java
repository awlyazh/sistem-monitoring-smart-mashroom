package com.example.smartmashroom;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NotifikasiActivity extends AppCompatActivity {

    private LinearLayout notificationContainer;
    private ImageButton btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        notificationContainer = findViewById(R.id.notification_container);
        btnDelete = findViewById(R.id.btn_delete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletedCount = 0;

                // Kita gunakan loop mundur agar tidak terjadi masalah saat menghapus elemen
                for (int i = notificationContainer.getChildCount() - 1; i >= 0; i--) {
                    View notifView = notificationContainer.getChildAt(i);

                    if (notifView instanceof LinearLayout) {
                        LinearLayout notifLayout = (LinearLayout) notifView;

                        // Cek apakah child pertama adalah CheckBox
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
    }
}
