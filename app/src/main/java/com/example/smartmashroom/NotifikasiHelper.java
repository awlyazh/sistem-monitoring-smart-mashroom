package com.example.smartmashroom;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class NotifikasiHelper {

    private static final String CHANNEL_ID = "SmartMushroom_Channel";
    private static final String CHANNEL_NAME = "Notifikasi Monitoring";
    private static final String CHANNEL_DESC = "Notifikasi suhu dan kelembaban jamur";

    public static void showNotification(Context context, String message) {
        // ✅ Cek izin notifikasi untuk Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.e("NotifikasiHelper", "Izin notifikasi belum diberikan. Notifikasi dibatalkan.");
                return;
            }
        }

        // ✅ Buat channel notifikasi untuk Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null && manager.getNotificationChannel(CHANNEL_ID) == null) {
                manager.createNotificationChannel(channel);
            }
        }

        // ✅ Bangun notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert) // Ganti jika punya icon sendiri
                .setContentTitle("SmartMushroom")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message)) // Untuk teks panjang
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // ✅ Tampilkan notifikasi
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        int notificationId = (int) System.currentTimeMillis(); // ID unik agar tidak tertimpa
        notificationManager.notify(notificationId, builder.build());

        Log.d("NotifikasiHelper", "Notifikasi berhasil ditampilkan: " + message);
    }
}