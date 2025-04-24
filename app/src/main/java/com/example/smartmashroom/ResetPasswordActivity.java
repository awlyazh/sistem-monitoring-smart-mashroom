package com.example.smartmashroom;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        EditText etNewPassword = findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ImageView ivToggleNewPassword = findViewById(R.id.ivToggleNewPassword);
        ImageView ivToggleConfirmPassword = findViewById(R.id.ivToggleConfirmPassword);
        Button btnSubmit = findViewById(R.id.btnSubmitNewPassword);

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

        // Validasi dan submit
        btnSubmit.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            } else if (newPassword.length() < 6) {
                etNewPassword.setError("Minimal 6 karakter");
            } else if (!newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("Sandi tidak cocok");
            } else {
                Toast.makeText(this, "Sandi berhasil diubah", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
