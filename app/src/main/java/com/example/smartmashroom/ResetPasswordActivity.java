package com.example.smartmashroom;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditText etEmail = findViewById(R.id.etEmail); // TAMBAHKAN FIELD EMAIL DI LAYOUT
        EditText etNewPassword = findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ImageView ivToggleNewPassword = findViewById(R.id.ivToggleNewPassword);
        ImageView ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword);
        Button btnSubmit = findViewById(R.id.btnSubmitNewPassword);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Toggle sandi baru
        ivToggleNewPassword.setOnClickListener(v -> {
            isNewPasswordVisible = !isNewPasswordVisible;
            if (isNewPasswordVisible) {
                etNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivToggleNewPassword.setImageResource(R.drawable.ic_eye_open);
            } else {
                etNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivToggleNewPassword.setImageResource(R.drawable.ic_eye_closed);
            }
            etNewPassword.setSelection(etNewPassword.getText().length());
        });

        // Toggle konfirmasi sandi
        ivToggleConfirmPassword.setOnClickListener(v -> {
            isConfirmPasswordVisible = !isConfirmPasswordVisible;
            if (isConfirmPasswordVisible) {
                etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_open);
            } else {
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                ivToggleConfirmPassword.setImageResource(R.drawable.ic_eye_closed);
            }
            etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        });

        // Validasi dan submit reset password via email
        btnSubmit.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Email harus diisi");
                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Link reset telah dikirim ke email Anda", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Gagal mengirim email reset: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
