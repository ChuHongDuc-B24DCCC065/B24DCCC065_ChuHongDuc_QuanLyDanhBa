package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Thiết kế giao diện hiện đại, tối giản đúng theo mẫu phác thảo:
 * - Avatar tròn lớn ở giữa kèm Họ tên & SĐT
 * - Nút gắn sao Yêu thích tương tác trực tiếp
 * - Hàng 3 nút hành động tròn: Gọi điện | Nhắn tin | Chỉnh sửa
 * - Thẻ thông tin Nhóm, Giới tính, Ghi chú
 * - Nút Xóa liên hệ ở dưới cùng
 */
public class ContactDetailActivity extends AppCompatActivity {

    private ImageView btnBackDetailHeader;
    private FrameLayout layoutDetailAvatar;
    private TextView tvDetailAvatarInitial;
    private TextView tvDetailName;
    private TextView tvDetailPhone;
    private LinearLayout btnToggleFavorite;
    private ImageView ivDetailStar;
    private TextView tvDetailFavoriteStatus;

    private FrameLayout btnActionCall;
    private FrameLayout btnActionMessage;
    private FrameLayout btnActionEdit;

    private TextView tvDetailGroup;
    private TextView tvDetailGender;
    private TextView tvDetailNote;
    private LinearLayout btnDeleteContactAction;

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
        btnToggleFavorite = findViewById(R.id.btnToggleFavorite);
        ivDetailStar = findViewById(R.id.ivDetailStar);
        tvDetailFavoriteStatus = findViewById(R.id.tvDetailFavoriteStatus);

        btnActionCall = findViewById(R.id.btnActionCall);
        btnActionMessage = findViewById(R.id.btnActionMessage);
        btnActionEdit = findViewById(R.id.btnActionEdit);

        tvDetailGroup = findViewById(R.id.tvDetailGroup);
        tvDetailGender = findViewById(R.id.tvDetailGender);
        tvDetailNote = findViewById(R.id.tvDetailNote);
        btnDeleteContactAction = findViewById(R.id.btnDeleteContactAction);
    }

    private void displayContactData() {
        if (currentContact == null) return;

        // Họ tên & Số điện thoại
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

        // Avatar tròn lớn màu pastel
        tvDetailAvatarInitial.setText(currentContact.getInitialLetter());
        tvDetailAvatarInitial.setTextColor(currentContact.getAvatarTextColor());

        GradientDrawable avatarDrawable = new GradientDrawable();
        avatarDrawable.setShape(GradientDrawable.OVAL);
        avatarDrawable.setColor(currentContact.getAvatarBgColor());
        layoutDetailAvatar.setBackground(avatarDrawable);

        updateFavoriteUI();
    }

    private void updateFavoriteUI() {
        if (currentContact.isFavorite()) {
            ivDetailStar.setImageResource(R.drawable.ic_star_filled_teal);
            tvDetailFavoriteStatus.setText(R.string.stats_favorite);
        } else {
            ivDetailStar.setImageResource(R.drawable.ic_star_outline_teal);
            tvDetailFavoriteStatus.setText(R.string.stats_favorite);
        }
    }

    private void setupListeners() {
        // Nút quay lại
        btnBackDetailHeader.setOnClickListener(v -> returnWithResult());

        // Chuyển đổi trạng thái Yêu thích
        btnToggleFavorite.setOnClickListener(v -> {
            if (currentContact != null) {
                boolean newFav = !currentContact.isFavorite();
                currentContact.setFavorite(newFav);
                updateFavoriteUI();
                if (contactPosition >= 0) {
                    ContactManager.getInstance().updateContact(contactPosition, currentContact);
                }
                Toast.makeText(this, newFav ? "Đã thêm vào yêu thích" : "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
        });

        // 1. Gọi điện: Intent ngầm định ACTION_DIAL
        btnActionCall.setOnClickListener(v -> {
            if (currentContact != null && currentContact.getPhoneNumber() != null) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + currentContact.getPhoneNumber()));
                startActivity(dialIntent);
            }
        });

        // 2. Nhắn tin: Intent ngầm định ACTION_SENDTO (sms)
        btnActionMessage.setOnClickListener(v -> {
            if (currentContact != null && currentContact.getPhoneNumber() != null) {
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
                smsIntent.setData(Uri.parse("smsto:" + currentContact.getPhoneNumber()));
                startActivity(smsIntent);
            }
        });

        // 3. Chỉnh sửa
        btnActionEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng chỉnh sửa thông tin liên hệ", Toast.LENGTH_SHORT).show();
        });

        // Nút Xóa liên hệ
        btnDeleteContactAction.setOnClickListener(v -> confirmAndDeleteContact());
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
