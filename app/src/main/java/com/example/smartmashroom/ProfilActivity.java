package com.example.smartmashroom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    private static final int REQUEST_NAMA = 1;
    private static final int REQUEST_EMAIL = 2;
    private static final int REQUEST_PASSWORD = 3;
    private static final int PICK_IMAGE = 100;

    LinearLayout itemNama, itemPassword, itemEmail;
    TextView tvNama, tvEmail, tvPassword;
    ImageView imageProfile, ivTogglePassword, btnEditPhoto;
    Button btnKeluar;

    String nama = "Soehardi";
    String email = "jamutriamirdho@gmail.com";
    String password = "";

    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Inisialisasi UI
        itemNama = findViewById(R.id.item_nama);
        itemPassword = findViewById(R.id.item_password);
        itemEmail = findViewById(R.id.item_email);
        tvNama = findViewById(R.id.tv_nama);
        tvEmail = findViewById(R.id.tv_email);
        tvPassword = findViewById(R.id.tv_password);
        imageProfile = findViewById(R.id.imageProfile);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
        btnEditPhoto = findViewById(R.id.btn_edit_photo);
        btnKeluar = findViewById(R.id.btn_keluar); // Tombol keluar akun

        // Ambil data dari SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        nama = prefs.getString("nama", nama);
        email = prefs.getString("email", email);
        password = prefs.getString("password", password);

        updateUI();

        // Klik ubah nama
        itemNama.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditNamaActivity.class);
            intent.putExtra("nama", nama);
            startActivityForResult(intent, REQUEST_NAMA);
        });

        // Klik ubah password
        itemPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditPasswordActivity.class);
            intent.putExtra("password", password);
            startActivityForResult(intent, REQUEST_PASSWORD);
        });

        // Klik ubah email
        itemEmail.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditEmailActivity.class);
            intent.putExtra("email", email);
            startActivityForResult(intent, REQUEST_EMAIL);
        });

        // Klik ubah foto profil via foto utama
        imageProfile.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE);
        });

        // Klik ubah foto profil via icon pensil
        btnEditPhoto.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE);
        });

        // Toggle password eye icon
        ivTogglePassword.setOnClickListener(v -> {
            if (tvPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                tvPassword.setTransformationMethod(null);
                ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
            } else {
                tvPassword.setTransformationMethod(new PasswordTransformationMethod());
                ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
            }
        });

        // Aksi tombol keluar akun
        btnKeluar.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
            editor.clear(); // hapus semua data
            editor.apply();

            // Arahkan ke halaman login
            Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
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
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();

            switch (requestCode) {
                case REQUEST_NAMA:
                    nama = data.getStringExtra("namaBaru");
                    editor.putString("nama", nama);
                    break;
                case REQUEST_EMAIL:
                    email = data.getStringExtra("emailBaru");
                    editor.putString("email", email);
                    break;
                case REQUEST_PASSWORD:
                    password = data.getStringExtra("passwordBaru");
                    editor.putString("password", password);
                    break;
                case PICK_IMAGE:
                    selectedImageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                        imageProfile.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

            editor.apply();
            updateUI();
        }
    }

    private void updateUI() {
        tvNama.setText(nama);
        tvEmail.setText(email);

        if (password.isEmpty()) {
            password = "123456"; // Set default jika kosong
        }

        tvPassword.setText(password);
        tvPassword.setTransformationMethod(new PasswordTransformationMethod());
        ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
    }
}
