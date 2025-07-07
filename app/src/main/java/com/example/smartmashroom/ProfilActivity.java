package com.example.smartmashroom;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    private static final int REQUEST_NAMA = 1;
    private static final int REQUEST_PASSWORD = 3;
    private static final int PICK_IMAGE = 100;

    LinearLayout itemNama, itemPassword;
    TextView tvNama, tvEmail, tvPassword;
    ImageView imageProfile, ivTogglePassword, btnEditPhoto;
    Button btnKeluar;

    String nama = "";  // Biarkan kosong, akan diisi dari EncryptedSharedPreferences
    String email = "";
    char[] passwordChars = null;

    Uri selectedImageUri;

    SharedPreferences securePrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_profil);

        itemNama = findViewById(R.id.item_nama);
        itemPassword = findViewById(R.id.item_password);
        tvNama = findViewById(R.id.tv_nama);
        tvEmail = findViewById(R.id.tv_email);
        tvPassword = findViewById(R.id.tv_password);
        imageProfile = findViewById(R.id.imageProfile);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
        btnEditPhoto = findViewById(R.id.btn_edit_photo);
        btnKeluar = findViewById(R.id.btn_keluar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            securePrefs = getSecurePrefs();
            nama = securePrefs.getString("nama", nama);
            String pass = securePrefs.getString("password", "");
            passwordChars = pass != null ? pass.toCharArray() : new char[0];
        }

        updateUI();

        itemNama.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditNamaActivity.class);
            intent.putExtra("nama", nama);
            startActivityForResult(intent, REQUEST_NAMA);
        });

        itemPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, EditPasswordActivity.class);
            intent.putExtra("password", new String(passwordChars));
            startActivityForResult(intent, REQUEST_PASSWORD);
        });

        imageProfile.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE);
        });

        btnEditPhoto.setOnClickListener(v -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE);
        });

        ivTogglePassword.setOnClickListener(v -> {
            // Tidak menampilkan password asli, tetap tampil bintang
            Toast.makeText(this, "Password disembunyikan demi keamanan", Toast.LENGTH_SHORT).show();
        });

        btnKeluar.setOnClickListener(v -> {
            if (securePrefs != null) {
                securePrefs.edit().clear().apply();
            }
            clearPasswordFromMemory();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

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

        if (resultCode == RESULT_OK && data != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SharedPreferences.Editor editor = securePrefs.edit();

            switch (requestCode) {
                case REQUEST_NAMA:
                    nama = data.getStringExtra("namaBaru");
                    editor.putString("nama", nama);
                    break;
                case REQUEST_PASSWORD:
                    String passBaru = data.getStringExtra("passwordBaru");
                    if (passBaru != null) {
                        clearPasswordFromMemory();
                        passwordChars = passBaru.toCharArray();
                        editor.putString("password", passBaru);
                    }
                    break;
                case PICK_IMAGE:
                    selectedImageUri = data.getData();
                    if (isImageValid(selectedImageUri)) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            imageProfile.setImageBitmap(bitmap);
                            uploadProfileImage(selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Format gambar tidak didukung!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

            editor.apply();
            updateUI();
        }
    }

    private void updateUI() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            email = currentUser.getEmail();
            tvEmail.setText(email);
        } else {
            tvEmail.setText("Tidak ada pengguna login");
        }

        tvNama.setText(nama);
        tvPassword.setText("********"); // Tidak menampilkan password asli
        tvPassword.setTransformationMethod(new PasswordTransformationMethod());
        ivTogglePassword.setImageResource(R.drawable.ic_eye_closed);
    }

    private void clearPasswordFromMemory() {
        if (passwordChars != null) {
            for (int i = 0; i < passwordChars.length; i++) {
                passwordChars[i] = '\0';
            }
        }
    }

    private boolean isImageValid(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        return type != null && (type.equals("jpg") || type.equals("jpeg") || type.equals("png"));
    }

    private void uploadProfileImage(Uri imageUri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileImageRef = storageRef.child("profile_images/" + user.getUid() + ".jpg");

        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot ->
                        Toast.makeText(ProfilActivity.this, "Foto Profil berhasil diupdate", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(ProfilActivity.this, "Gagal mengupdate foto profil", Toast.LENGTH_SHORT).show());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private SharedPreferences getSecurePrefs() {
        try {
            MasterKey masterKey = new MasterKey.Builder(this)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            return EncryptedSharedPreferences.create(
                    this,
                    "secure_user_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
            return getSharedPreferences("fallback_user_prefs", MODE_PRIVATE);
        }
    }
}
