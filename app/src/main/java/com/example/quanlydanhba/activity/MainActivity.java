package com.example.quanlydanhba.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.adapter.ContactAdapter;
import com.example.quanlydanhba.data.ContactManager;
import com.example.quanlydanhba.model.Contact;

import java.util.ArrayList;

/**
 * Màn hình 2: Danh sách liên hệ (MainActivity)
 * - Khối thống kê: hiển thị tổng số liên hệ và số liên hệ yêu thích
 * - Danh sách liên hệ (RecyclerView): ảnh đại diện tròn, họ tên, số điện thoại, nhóm, dấu sao
 * - Nút "+ Thêm liên hệ": mở AddContactActivity
 * - Nhấn vào một liên hệ: mở ContactDetailActivity
 */
public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    private TextView tvTotalCount;
    private TextView tvFavoriteCount;
    private RecyclerView rvContacts;
    private TextView tvEmpty;
    private TextView tvHintDetail;
    private Button btnAddContact;

    private ArrayList<Contact> contactList;
    private ContactAdapter adapter;

    // Launcher mở màn hình Thêm liên hệ
    private final ActivityResultLauncher<Intent> addContactLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Contact newContact = (Contact) result.getData().getSerializableExtra("new_contact");
                    if (newContact != null) {
                        // Nếu ContactManager chưa thêm thì thêm vào danh sách
                        if (!contactList.contains(newContact)) {
                            contactList.add(newContact);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    updateStatistics();
                }
            });

    // Launcher mở màn hình Chi tiết liên hệ
    private final ActivityResultLauncher<Intent> detailContactLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    boolean isDeleted = result.getData().getBooleanExtra("is_deleted", false);
                    int position = result.getData().getIntExtra("position", -1);

                    if (isDeleted && position >= 0 && position < contactList.size()) {
                        // Xóa liên hệ
                        contactList.remove(position);
                        adapter.notifyItemRemoved(position);
                    } else if (position >= 0 && position < contactList.size()) {
                        // Cập nhật trạng thái liên hệ (ví dụ: yêu thích)
                        Contact updatedContact = (Contact) result.getData().getSerializableExtra("updated_contact");
                        if (updatedContact != null) {
                            contactList.set(position, updatedContact);
                        }
                        adapter.notifyItemChanged(position);
                    }
                    updateStatistics();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupListeners();
        updateStatistics();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đồng bộ dữ liệu và cập nhật thống kê khi quay lại màn hình
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        updateStatistics();
    }

    private void initViews() {
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        rvContacts = findViewById(R.id.rvContacts);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvHintDetail = findViewById(R.id.tvHintDetail);
        btnAddContact = findViewById(R.id.btnAddContact);
    }

    private void setupRecyclerView() {
        contactList = ContactManager.getInstance().getContacts();
        adapter = new ContactAdapter(contactList, this);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            addContactLauncher.launch(intent);
        });
    }

    /**
     * Cập nhật khối thống kê: tổng số liên hệ và số liên hệ yêu thích
     */
    private void updateStatistics() {
        int total = contactList.size();
        int favorites = 0;
        for (Contact c : contactList) {
            if (c.isFavorite()) {
                favorites++;
            }
        }

        tvTotalCount.setText(String.valueOf(total));
        tvFavoriteCount.setText(String.valueOf(favorites));

        if (total == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvHintDetail.setVisibility(View.GONE);
            rvContacts.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            tvHintDetail.setVisibility(View.VISIBLE);
            rvContacts.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Xử lý khi nhấn vào một dòng liên hệ: mở ContactDetailActivity bằng Intent
     */
    @Override
    public void onContactClick(int position, Contact contact) {
        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        intent.putExtra("contact", contact);
        intent.putExtra("position", position);
        detailContactLauncher.launch(intent);
    }
}
