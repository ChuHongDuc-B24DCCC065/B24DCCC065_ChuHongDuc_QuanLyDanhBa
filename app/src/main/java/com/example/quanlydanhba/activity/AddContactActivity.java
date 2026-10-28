package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.data.ContactManager;
import com.example.quanlydanhba.model.Contact;

/**
 * Màn hình 3: Thêm liên hệ (AddContactActivity)
 * Thiết kế giao diện hiện đại theo phong cách tối giản.
 * - Nhập họ tên & số điện thoại
 * - Chọn nhóm qua Spinner bo tròn
 * - Chọn giới tính dạng Segmented Tabs (Nam | Nữ | Khác)
 * - Checkbox Yêu thích
 * - Nhập ghi chú
 * - Nút Lưu & nút Hủy
 */
public class AddContactActivity extends AppCompatActivity {

    private ImageView btnBackAdd;
    private TextView btnCancelAdd;
    private EditText etName;
    private EditText etPhone;
    private Spinner spGroup;
    private TextView tabGenderMale;
    private TextView tabGenderFemale;
    private TextView tabGenderOther;
    private LinearLayout layoutFavoriteToggle;
    private CheckBox cbFavorite;
    private EditText etNote;
    private Button btnSaveContact;

    private String selectedGender = "Nam";
    private static final String[] GROUPS = {"Bạn bè", "Gia đình", "Công việc", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initViews();
        setupSpinner();
        setupGenderTabs();
        setupListeners();
    }

    private void initViews() {
        btnBackAdd = findViewById(R.id.btnBackAdd);
        btnCancelAdd = findViewById(R.id.btnCancelAdd);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        spGroup = findViewById(R.id.spGroup);
        tabGenderMale = findViewById(R.id.tabGenderMale);
        tabGenderFemale = findViewById(R.id.tabGenderFemale);
        tabGenderOther = findViewById(R.id.tabGenderOther);
        layoutFavoriteToggle = findViewById(R.id.layoutFavoriteToggle);
        cbFavorite = findViewById(R.id.cbFavorite);
        etNote = findViewById(R.id.etNote);
        btnSaveContact = findViewById(R.id.btnSaveContact);
    }

    private void setupSpinner() {
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                GROUPS
        );
        groupAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spGroup.setAdapter(groupAdapter);
        spGroup.setSelection(0); // "Bạn bè" mặc định
    }

    private void setupGenderTabs() {
        tabGenderMale.setOnClickListener(v -> selectGender("Nam"));
        tabGenderFemale.setOnClickListener(v -> selectGender("Nữ"));
        tabGenderOther.setOnClickListener(v -> selectGender("Khác"));
        selectGender("Nam"); // Mặc định chọn Nam
    }

    private void selectGender(String gender) {
        selectedGender = gender;

        // Reset cả 3 tabs về unselected
        tabGenderMale.setBackgroundResource(R.drawable.bg_gender_unselected);
        tabGenderMale.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabGenderMale.setTypeface(null, Typeface.NORMAL);

        tabGenderFemale.setBackgroundResource(R.drawable.bg_gender_unselected);
        tabGenderFemale.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabGenderFemale.setTypeface(null, Typeface.NORMAL);

        tabGenderOther.setBackgroundResource(R.drawable.bg_gender_unselected);
        tabGenderOther.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabGenderOther.setTypeface(null, Typeface.NORMAL);

        // Highlight tab được chọn
        if ("Nam".equals(gender)) {
            tabGenderMale.setBackgroundResource(R.drawable.bg_gender_selected);
            tabGenderMale.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tabGenderMale.setTypeface(null, Typeface.BOLD);
        } else if ("Nữ".equals(gender)) {
            tabGenderFemale.setBackgroundResource(R.drawable.bg_gender_selected);
            tabGenderFemale.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tabGenderFemale.setTypeface(null, Typeface.BOLD);
        } else {
            tabGenderOther.setBackgroundResource(R.drawable.bg_gender_selected);
            tabGenderOther.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tabGenderOther.setTypeface(null, Typeface.BOLD);
        }
    }

    private void setupListeners() {
        btnBackAdd.setOnClickListener(v -> finish());
        btnCancelAdd.setOnClickListener(v -> finish());

        // Cho phép bấm cả hàng để bật/tắt checkbox yêu thích
        layoutFavoriteToggle.setOnClickListener(v -> cbFavorite.setChecked(!cbFavorite.isChecked()));

        btnSaveContact.setOnClickListener(v -> handleSaveContact());
    }

    private void handleSaveContact() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        // 1. Kiểm tra họ và tên không được để trống
        if (name.isEmpty()) {
            etName.setError(getString(R.string.error_name_empty));
            etName.requestFocus();
            Toast.makeText(this, R.string.error_name_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra số điện thoại không được để trống
        if (phone.isEmpty()) {
            etPhone.setError(getString(R.string.error_phone_empty));
            etPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Kiểm tra số điện thoại hợp lệ (10 chữ số và bắt đầu bằng số 0)
        if (!phone.matches("^0[0-9]{9}$")) {
            etPhone.setError(getString(R.string.error_phone_invalid));
            etPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Kiểm tra số điện thoại đã tồn tại chưa
        if (ContactManager.getInstance().isPhoneExists(phone, -1)) {
            etPhone.setError(getString(R.string.error_phone_exists));
            etPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_exists, Toast.LENGTH_SHORT).show();
            return;
        }

        String group = spGroup.getSelectedItem().toString();
        boolean isFavorite = cbFavorite.isChecked();
        String note = etNote.getText().toString().trim();

        Contact newContact = new Contact(name, phone, group, selectedGender, isFavorite, note);
        ContactManager.getInstance().addContact(newContact);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_contact", newContact);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, R.string.msg_add_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
