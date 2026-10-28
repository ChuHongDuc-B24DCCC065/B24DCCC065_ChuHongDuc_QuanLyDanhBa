package com.example.quanlydanhba.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.adapter.ContactAdapter;
import com.example.quanlydanhba.data.ContactManager;
import com.example.quanlydanhba.model.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Màn hình 2: Danh sách liên hệ (MainActivity)
 * Các tính năng chính:
 * 1. Khối thống kê kiêm Tag lọc: Bấm vào [Yêu thích] để lọc người yêu thích, bấm [Liên hệ] để hiện tất cả
 * 2. Thanh tìm kiếm thời gian thực (Search bar) theo tên hoặc số điện thoại
 * 3. Danh sách liên hệ (RecyclerView)
 * 4. Nút Floating Action Button (+) mở Thêm liên hệ
 * 5. Mở Chi tiết liên hệ (xem, sửa, gọi điện, nhắn tin, xóa)
 */
public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactClickListener {

    // Views thống kê & lọc
    private LinearLayout layoutTabAll;
    private TextView tvTotalCount;
    private TextView tvLabelAll;

    private LinearLayout layoutTabFavorite;
    private TextView tvFavoriteCount;
    private TextView tvLabelFavorite;

    // Search bar
    private EditText etSearch;
    private ImageView btnClearSearch;
    private TextView tvSectionTitle;

    // List & Adapter
    private RecyclerView rvContacts;
    private TextView tvEmpty;
    private TextView tvHintDetail;
    private FloatingActionButton fabAddContact;

    private ArrayList<Contact> masterList;
    private final ArrayList<Contact> displayedList = new ArrayList<>();
    private ContactAdapter adapter;

    // Trạng thái lọc
    private boolean isFilterFavoriteOnly = false;

    // Launcher mở màn hình Thêm liên hệ
    private final ActivityResultLauncher<Intent> addContactLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Contact newContact = (Contact) result.getData().getSerializableExtra("new_contact");
                    if (newContact != null && !masterList.contains(newContact)) {
                        masterList.add(newContact);
                    }
                    updateStatistics();
                    applyFilter();
                }
            });

    // Launcher mở màn hình Chi tiết liên hệ
    private final ActivityResultLauncher<Intent> detailContactLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    boolean isDeleted = result.getData().getBooleanExtra("is_deleted", false);
                    int position = result.getData().getIntExtra("position", -1);

                    if (isDeleted && position >= 0 && position < masterList.size()) {
                        masterList.remove(position);
                    } else if (position >= 0 && position < masterList.size()) {
                        Contact updatedContact = (Contact) result.getData().getSerializableExtra("updated_contact");
                        if (updatedContact != null) {
                            masterList.set(position, updatedContact);
                        }
                    }
                    updateStatistics();
                    applyFilter();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupListeners();
        setupSearch();
        updateStatistics();
        applyFilter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
        applyFilter();
    }

    private void initViews() {
        layoutTabAll = findViewById(R.id.layoutTabAll);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvLabelAll = findViewById(R.id.tvLabelAll);

        layoutTabFavorite = findViewById(R.id.layoutTabFavorite);
        tvFavoriteCount = findViewById(R.id.tvFavoriteCount);
        tvLabelFavorite = findViewById(R.id.tvLabelFavorite);

        etSearch = findViewById(R.id.etSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        tvSectionTitle = findViewById(R.id.tvSectionTitle);

        rvContacts = findViewById(R.id.rvContacts);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvHintDetail = findViewById(R.id.tvHintDetail);
        fabAddContact = findViewById(R.id.fabAddContact);
    }

    private void setupRecyclerView() {
        masterList = ContactManager.getInstance().getContacts();
        displayedList.clear();
        displayedList.addAll(masterList);

        adapter = new ContactAdapter(displayedList, this);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(adapter);
    }

    private void setupListeners() {
        // Bấm vào tab "Tất cả liên hệ"
        layoutTabAll.setOnClickListener(v -> {
            if (isFilterFavoriteOnly) {
                isFilterFavoriteOnly = false;
                updateTabUI();
                applyFilter();
            }
        });

        // Bấm vào tab "Yêu thích" để lọc danh sách
        layoutTabFavorite.setOnClickListener(v -> {
            if (!isFilterFavoriteOnly) {
                isFilterFavoriteOnly = true;
                updateTabUI();
                applyFilter();
            }
        });

        // Nút mở màn hình Thêm liên hệ
        fabAddContact.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            addContactLauncher.launch(intent);
        });

        // Nút xóa nhanh nội dung tìm kiếm
        btnClearSearch.setOnClickListener(v -> etSearch.setText(""));
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnClearSearch.setVisibility(View.VISIBLE);
                } else {
                    btnClearSearch.setVisibility(View.GONE);
                }
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Cập nhật giao diện của 2 tab lọc Tất cả / Yêu thích
     */
    private void updateTabUI() {
        if (isFilterFavoriteOnly) {
            // Tab Yêu thích được kích hoạt
            layoutTabFavorite.setBackgroundResource(R.drawable.bg_tab_filter_active);
            tvLabelFavorite.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tvLabelFavorite.setTypeface(null, Typeface.BOLD);

            layoutTabAll.setBackgroundResource(R.drawable.bg_tab_filter_inactive);
            tvLabelAll.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            tvLabelAll.setTypeface(null, Typeface.NORMAL);

            tvSectionTitle.setText(R.string.favorites_list_header);
        } else {
            // Tab Tất cả được kích hoạt
            layoutTabAll.setBackgroundResource(R.drawable.bg_tab_filter_active);
            tvLabelAll.setTextColor(ContextCompat.getColor(this, R.color.primary_teal));
            tvLabelAll.setTypeface(null, Typeface.BOLD);

            layoutTabFavorite.setBackgroundResource(R.drawable.bg_tab_filter_inactive);
            tvLabelFavorite.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
            tvLabelFavorite.setTypeface(null, Typeface.NORMAL);

            tvSectionTitle.setText(R.string.contact_list_header);
        }
    }

    /**
     * Cập nhật số lượng trên 2 khối thống kê
     */
    private void updateStatistics() {
        int total = masterList.size();
        int favorites = 0;
        for (Contact c : masterList) {
            if (c.isFavorite()) {
                favorites++;
            }
        }

        tvTotalCount.setText(String.valueOf(total));
        tvFavoriteCount.setText(String.valueOf(favorites));
    }

    /**
     * Lọc danh sách theo từ khóa tìm kiếm và chế độ xem Yêu thích
     */
    private void applyFilter() {
        String query = etSearch.getText().toString().trim().toLowerCase();
        String cleanQuery = query.replaceAll("\\s+", "");

        displayedList.clear();

        for (Contact c : masterList) {
            // Nếu đang chọn tab Yêu thích mà liên hệ không yêu thích thì bỏ qua
            if (isFilterFavoriteOnly && !c.isFavorite()) {
                continue;
            }

            // Nếu có từ khóa tìm kiếm thì kiểm tra Tên hoặc Số điện thoại
            if (!query.isEmpty()) {
                boolean matchName = c.getName() != null && c.getName().toLowerCase().contains(query);
                boolean matchPhone = c.getPhoneNumber() != null &&
                        c.getPhoneNumber().replaceAll("\\s+", "").contains(cleanQuery);

                if (!matchName && !matchPhone) {
                    continue;
                }
            }

            displayedList.add(c);
        }

        adapter.notifyDataSetChanged();

        // Xử lý thông báo khi danh sách rỗng
        if (displayedList.isEmpty()) {
            rvContacts.setVisibility(View.GONE);
            tvHintDetail.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);

            if (masterList.isEmpty()) {
                tvEmpty.setText(R.string.empty_contact_list);
            } else if (isFilterFavoriteOnly && ContactManager.getInstance().getFavoriteCount() == 0) {
                tvEmpty.setText(R.string.empty_favorites_list);
            } else if (!query.isEmpty()) {
                tvEmpty.setText(R.string.empty_search_result);
            } else {
                tvEmpty.setText(R.string.empty_contact_list);
            }
        } else {
            rvContacts.setVisibility(View.VISIBLE);
            tvHintDetail.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * Xử lý khi nhấn vào một dòng liên hệ trong danh sách
     */
    @Override
    public void onContactClick(int position, Contact contact) {
        // Tìm vị trí gốc trong masterList
        int masterPosition = masterList.indexOf(contact);
        if (masterPosition == -1) {
            masterPosition = position;
        }

        Intent intent = new Intent(MainActivity.this, ContactDetailActivity.class);
        intent.putExtra("contact", contact);
        intent.putExtra("position", masterPosition);
        detailContactLauncher.launch(intent);
    }
}
