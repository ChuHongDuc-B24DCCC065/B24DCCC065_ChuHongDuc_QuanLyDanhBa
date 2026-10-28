package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.data.ContactManager;
import com.example.quanlydanhba.model.Contact;

/**
 * Màn hình 4: Chi tiết liên hệ (ContactDetailActivity)
 * - Hiển thị đầy đủ thông tin: họ tên, ảnh đại diện chữ cái, SĐT, nhóm, giới tính, ghi chú, yêu thích
 * - CheckBox Yêu thích: có thể thay đổi trực tiếp, cập nhật danh sách và thống kê khi quay lại
 * - Nút "Gọi điện": Intent ngầm định (ACTION_DIAL) mở màn hình quay số
 * - Nút "Quay lại": đóng màn hình và trả về kết quả
 * - Nút "Xóa liên hệ": xác nhận xóa, xóa khỏi danh sách và cập nhật thống kê
 */
public class ContactDetailActivity extends AppCompatActivity {

    private ImageView btnBackDetailHeader;
    private FrameLayout layoutDetailAvatar;
    private TextView tvDetailAvatarInitial;
    private TextView tvDetailName;
    private TextView tvDetailPhone;
    private TextView tvDetailGroup;
    private TextView tvDetailGender;
    private TextView tvDetailNote;
    private CheckBox cbDetailFavorite;
    private Button btnCall;
    private Button btnBack;
    private Button btnDelete;

    private Contact currentContact;
    private int contactPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        getIntentData();
        initViews();
        displayContactData();
        setupListeners();
        setupBackPressedHandler();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            currentContact = (Contact) intent.getSerializableExtra("contact");
            contactPosition = intent.getIntExtra("position", -1);
        }
    }

    private void initViews() {
        btnBackDetailHeader = findViewById(R.id.btnBackDetailHeader);
        layoutDetailAvatar = findViewById(R.id.layoutDetailAvatar);
        tvDetailAvatarInitial = findViewById(R.id.tvDetailAvatarInitial);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailPhone = findViewById(R.id.tvDetailPhone);
        tvDetailGroup = findViewById(R.id.tvDetailGroup);
        tvDetailGender = findViewById(R.id.tvDetailGender);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        cbDetailFavorite = findViewById(R.id.cbDetailFavorite);
        btnCall = findViewById(R.id.btnCall);
        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void displayContactData() {
        if (currentContact == null) return;

        // Họ tên & SĐT
        tvDetailName.setText(currentContact.getName());
        tvDetailPhone.setText(currentContact.getPhoneNumber());

        // Nhóm & Giới tính
        tvDetailGroup.setText(currentContact.getGroup());
        tvDetailGender.setText(currentContact.getGender());

        // Ghi chú
        if (currentContact.getNote() != null && !currentContact.getNote().trim().isEmpty()) {
            tvDetailNote.setText(currentContact.getNote());
        } else {
            tvDetailNote.setText("(Không có ghi chú)");
        }

        // CheckBox yêu thích
        cbDetailFavorite.setChecked(currentContact.isFavorite());

        // Avatar tròn chứa ký tự đầu
        tvDetailAvatarInitial.setText(currentContact.getInitialLetter());
        GradientDrawable avatarDrawable = new GradientDrawable();
        avatarDrawable.setShape(GradientDrawable.OVAL);
        avatarDrawable.setColor(currentContact.getAvatarColor());
        layoutDetailAvatar.setBackground(avatarDrawable);
    }

    private void setupListeners() {
        // Nút quay lại trên thanh tiêu đề
        btnBackDetailHeader.setOnClickListener(v -> returnWithResult());

        // Nút Quay lại bên dưới
        btnBack.setOnClickListener(v -> returnWithResult());

        // Thay đổi trạng thái Yêu thích
        cbDetailFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (currentContact != null) {
                currentContact.setFavorite(isChecked);
                if (contactPosition >= 0) {
                    ContactManager.getInstance().updateContact(contactPosition, currentContact);
                }
            }
        });

        // Nút Gọi điện: sử dụng Intent ngầm định ACTION_DIAL
        btnCall.setOnClickListener(v -> {
            if (currentContact != null && currentContact.getPhoneNumber() != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + currentContact.getPhoneNumber()));
                startActivity(dialIntent);
            }
        });

        // Nút Xóa liên hệ
        btnDelete.setOnClickListener(v -> confirmAndDeleteContact());
    }

    private void setupBackPressedHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                returnWithResult();
            }
        });
    }

    private void returnWithResult() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("updated_contact", currentContact);
        resultIntent.putExtra("position", contactPosition);
        resultIntent.putExtra("is_deleted", false);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void confirmAndDeleteContact() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_msg)
                .setPositiveButton(R.string.action_yes, (dialog, which) -> {
                    if (contactPosition >= 0) {
                        ContactManager.getInstance().deleteContact(contactPosition);
                    }
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("position", contactPosition);
                    resultIntent.putExtra("is_deleted", true);
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(this, R.string.msg_delete_success, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }
}
