package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.data.ContactManager;
import com.example.quanlydanhba.model.Contact;

/**
 * Màn hình 3: Thêm liên hệ (AddContactActivity)
 * - Nhập họ tên (không được để trống)
 * - Nhập số điện thoại (10 số, bắt đầu bằng 0, không trùng trong danh bạ)
 * - Chọn nhóm (Spinner: Gia đình, Bạn bè, Công việc, Khác)
 * - Chọn giới tính (RadioGroup: Nam, Nữ, Khác - mặc định Nam)
 * - Chọn yêu thích (CheckBox)
 * - Nhập ghi chú (EditText nhiều dòng)
 * - Nút Lưu: kiểm tra hợp lệ, tạo đối tượng Contact, trả về MainActivity
 */
public class AddContactActivity extends AppCompatActivity {

    private ImageView btnBackAdd;
    private EditText etName;
    private EditText etPhone;
    private Spinner spGroup;
    private RadioGroup rgGender;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private RadioButton rbOther;
    private CheckBox cbFavorite;
    private EditText etNote;
    private Button btnSaveContact;

    private static final String[] GROUPS = {"Gia đình", "Bạn bè", "Công việc", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        initViews();
        setupSpinner();
        setupListeners();
    }

    private void initViews() {
        btnBackAdd = findViewById(R.id.btnBackAdd);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        spGroup = findViewById(R.id.spGroup);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        cbFavorite = findViewById(R.id.cbFavorite);
        etNote = findViewById(R.id.etNote);
        btnSaveContact = findViewById(R.id.btnSaveContact);

        // Mặc định chọn Nam theo đề bài
        rbMale.setChecked(true);
    }

    private void setupSpinner() {
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                GROUPS
        );
        groupAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spGroup.setAdapter(groupAdapter);

        // Mặc định chọn "Bạn bè" như trong phác thảo Hình 5
        spGroup.setSelection(1);
    }

    private void setupListeners() {
        btnBackAdd.setOnClickListener(v -> finish());
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

        // 4. Kiểm tra số điện thoại đã tồn tại trong danh bạ chưa
        if (ContactManager.getInstance().isPhoneExists(phone, -1)) {
            etPhone.setError(getString(R.string.error_phone_exists));
            etPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_exists, Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy nhóm đã chọn
        String group = spGroup.getSelectedItem().toString();

        // Lấy giới tính đã chọn
        String gender = "Nam";
        int checkedId = rgGender.getCheckedRadioButtonId();
        if (checkedId == R.id.rbFemale) {
            gender = "Nữ";
        } else if (checkedId == R.id.rbOther) {
            gender = "Khác";
        }

        // Trạng thái yêu thích và ghi chú
        boolean isFavorite = cbFavorite.isChecked();
        String note = etNote.getText().toString().trim();

        // Tạo đối tượng Contact
        Contact newContact = new Contact(name, phone, group, gender, isFavorite, note);

        // Lưu vào ContactManager
        ContactManager.getInstance().addContact(newContact);

        // Trả kết quả về cho MainActivity qua Intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra("new_contact", newContact);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, R.string.msg_add_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
