package com.example.quanlydanhba.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
 * Màn hình Chỉnh sửa thông tin liên hệ (EditContactActivity)
 * - Tự động điền dữ liệu hiện tại của liên hệ
 * - Cho phép sửa họ tên, số điện thoại, nhóm, giới tính, yêu thích, ghi chú
 * - Kiểm tra trùng số điện thoại (loại trừ chính liên hệ đang sửa)
 * - Cập nhật dữ liệu vào ContactManager và trả kết quả về
 */
public class EditContactActivity extends AppCompatActivity {

    private ImageView btnBackEdit;
    private TextView btnCancelEdit;
    private EditText etEditName;
    private EditText etEditPhone;
    private Spinner spEditGroup;
    private TextView tabEditGenderMale;
    private TextView tabEditGenderFemale;
    private TextView tabEditGenderOther;
    private LinearLayout layoutEditFavoriteToggle;
    private CheckBox cbEditFavorite;
    private EditText etEditNote;
    private Button btnSaveEditContact;

    private String selectedGender = "Nam";
    private Contact originalContact;
    private int contactPosition = -1;

    private static final String[] GROUPS = {"Bạn bè", "Gia đình", "Công việc", "Khác"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        getIntentData();
        initViews();
        setupSpinner();
        setupGenderTabs();
        populateContactData();
        setupListeners();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            originalContact = (Contact) intent.getSerializableExtra("contact");
            contactPosition = intent.getIntExtra("position", -1);
        }
    }

    private void initViews() {
        btnBackEdit = findViewById(R.id.btnBackEdit);
        btnCancelEdit = findViewById(R.id.btnCancelEdit);
        etEditName = findViewById(R.id.etEditName);
        etEditPhone = findViewById(R.id.etEditPhone);
        spEditGroup = findViewById(R.id.spEditGroup);
        tabEditGenderMale = findViewById(R.id.tabEditGenderMale);
        tabEditGenderFemale = findViewById(R.id.tabEditGenderFemale);
        tabEditGenderOther = findViewById(R.id.tabEditGenderOther);
        layoutEditFavoriteToggle = findViewById(R.id.layoutEditFavoriteToggle);
        cbEditFavorite = findViewById(R.id.cbEditFavorite);
        etEditNote = findViewById(R.id.etEditNote);
        btnSaveEditContact = findViewById(R.id.btnSaveEditContact);
    }

    private void setupSpinner() {
        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                GROUPS
        );
        groupAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spEditGroup.setAdapter(groupAdapter);
    }

    private void setupGenderTabs() {
        tabEditGenderMale.setOnClickListener(v -> selectGender("Nam"));
        tabEditGenderFemale.setOnClickListener(v -> selectGender("Nữ"));
        tabEditGenderOther.setOnClickListener(v -> selectGender("Khác"));
    }

    private void selectGender(String gender) {
        selectedGender = gender;

        tabEditGenderMale.setBackgroundResource(R.drawable.bg_gender_unselected);
        tabEditGenderMale.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabEditGenderMale.setTypeface(null, Typeface.NORMAL);

        tabEditGenderFemale.setBackgroundResource(R.drawable.bg_gender_unselected);
        tabEditGenderFemale.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabEditGenderFemale.setTypeface(null, Typeface.NORMAL);

        tabEditGenderOther.setBackgroundResource(R.drawable.bg_gender_unselected);
        tabEditGenderOther.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        tabEditGenderOther.setTypeface(null, Typeface.NORMAL);

        if ("Nam".equals(gender)) {
            tabEditGenderMale.setBackgroundResource(R.drawable.bg_gender_selected);
            tabEditGenderMale.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tabEditGenderMale.setTypeface(null, Typeface.BOLD);
        } else if ("Nữ".equals(gender)) {
            tabEditGenderFemale.setBackgroundResource(R.drawable.bg_gender_selected);
            tabEditGenderFemale.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tabEditGenderFemale.setTypeface(null, Typeface.BOLD);
        } else {
            tabEditGenderOther.setBackgroundResource(R.drawable.bg_gender_selected);
            tabEditGenderOther.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tabEditGenderOther.setTypeface(null, Typeface.BOLD);
        }
    }

    private void populateContactData() {
        if (originalContact == null) return;

        etEditName.setText(originalContact.getName());
        etEditPhone.setText(originalContact.getPhoneNumber());
        etEditNote.setText(originalContact.getNote());
        cbEditFavorite.setChecked(originalContact.isFavorite());

        // Chọn nhóm tương ứng
        String currentGroup = originalContact.getGroup();
        for (int i = 0; i < GROUPS.length; i++) {
            if (GROUPS[i].equalsIgnoreCase(currentGroup)) {
                spEditGroup.setSelection(i);
                break;
            }
        }

        // Chọn giới tính tương ứng
        selectGender(originalContact.getGender());
    }

    private void setupListeners() {
        btnBackEdit.setOnClickListener(v -> finish());
        btnCancelEdit.setOnClickListener(v -> finish());
        layoutEditFavoriteToggle.setOnClickListener(v -> cbEditFavorite.setChecked(!cbEditFavorite.isChecked()));
        btnSaveEditContact.setOnClickListener(v -> handleSaveEdit());
    }

    private void handleSaveEdit() {
        String name = etEditName.getText().toString().trim();
        String phone = etEditPhone.getText().toString().trim();

        // 1. Kiểm tra họ và tên
        if (name.isEmpty()) {
            etEditName.setError(getString(R.string.error_name_empty));
            etEditName.requestFocus();
            Toast.makeText(this, R.string.error_name_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra số điện thoại
        if (phone.isEmpty()) {
            etEditPhone.setError(getString(R.string.error_phone_empty));
            etEditPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Số điện thoại phải gồm 10 số và bắt đầu bằng số 0
        if (!phone.matches("^0[0-9]{9}$")) {
            etEditPhone.setError(getString(R.string.error_phone_invalid));
            etEditPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_invalid, Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Kiểm tra trùng số điện thoại (loại trừ chính liên hệ đang sửa)
        if (ContactManager.getInstance().isPhoneExists(phone, contactPosition)) {
            etEditPhone.setError(getString(R.string.error_phone_exists));
            etEditPhone.requestFocus();
            Toast.makeText(this, R.string.error_phone_exists, Toast.LENGTH_SHORT).show();
            return;
        }

        String group = spEditGroup.getSelectedItem().toString();
        boolean isFavorite = cbEditFavorite.isChecked();
        String note = etEditNote.getText().toString().trim();

        Contact updatedContact = new Contact(name, phone, group, selectedGender, isFavorite, note);

        if (contactPosition >= 0) {
            ContactManager.getInstance().updateContact(contactPosition, updatedContact);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("updated_contact", updatedContact);
        resultIntent.putExtra("position", contactPosition);
        setResult(RESULT_OK, resultIntent);

        Toast.makeText(this, R.string.msg_edit_success, Toast.LENGTH_SHORT).show();
        finish();
    }
}
