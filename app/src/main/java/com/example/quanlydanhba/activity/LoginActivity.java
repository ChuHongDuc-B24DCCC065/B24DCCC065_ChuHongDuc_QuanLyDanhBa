package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydanhba.R;

/**
 * Màn hình 1: Đăng nhập (LoginActivity)
 * Tài khoản cố định: admin / 123456
 * Đúng tài khoản: chuyển sang MainActivity và finish()
 * Sai tài khoản hoặc bỏ trống: hiển thị thông báo lỗi
 */
public class LoginActivity extends AppCompatActivity {

    // Tài khoản cố định theo đề bài
    private static final String FIXED_USERNAME = "admin";
    private static final String FIXED_PASSWORD = "123456";

    private EditText etUsername;
    private EditText etPassword;
    private TextView tvError;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 1. Kiểm tra bỏ trống
        if (username.isEmpty() || password.isEmpty()) {
            tvError.setText(R.string.login_error_empty);
            tvError.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.login_error_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. So sánh thông tin nhập với tài khoản cố định
        if (FIXED_USERNAME.equals(username) && FIXED_PASSWORD.equals(password)) {
            tvError.setVisibility(View.GONE);
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();

            // Chuyển sang MainActivity bằng Intent và gọi finish() để nút Back không quay lại
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Sai tên đăng nhập hoặc mật khẩu
            tvError.setText(R.string.login_error_invalid);
            tvError.setVisibility(View.VISIBLE);
            Toast.makeText(this, R.string.login_error_invalid, Toast.LENGTH_SHORT).show();
        }
    }
}
