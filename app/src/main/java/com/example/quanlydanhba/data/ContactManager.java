package com.example.quanlydanhba.data;

import com.example.quanlydanhba.model.Contact;

import java.util.ArrayList;

/**
 * Quản lý danh sách liên hệ trong bộ nhớ ứng dụng (ArrayList<Contact>).
 * Cung cấp dữ liệu mẫu ban đầu theo đúng yêu cầu đề bài.
 */
public class ContactManager {
    private static ContactManager instance;
    private final ArrayList<Contact> contactList;

    private ContactManager() {
        contactList = new ArrayList<>();
        initSampleData();
    }

    public static synchronized ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
        }
        return instance;
    }

    /**
     * Khởi tạo 3 liên hệ mẫu ban đầu theo đề bài:
     * 1. Nguyễn Văn An | 0901234567 | Bạn bè | Không
     * 2. Trần Thị Bình | 0912345678 | Gia đình | Có
     * 3. Lê Minh Châu | 0987654321 | Công việc | Không
     */
    private void initSampleData() {
        contactList.add(new Contact(
                "Nguyễn Văn An",
                "0901234567",
                "Bạn bè",
                "Nam",
                false,
                ""
        ));
        contactList.add(new Contact(
                "Trần Thị Bình",
                "0912345678",
                "Gia đình",
                "Nữ",
                true,
                "Chị gái, hay gọi buổi tối"
        ));
        contactList.add(new Contact(
                "Lê Minh Châu",
                "0987654321",
                "Công việc",
                "Khác",
                false,
                ""
        ));
    }

    public ArrayList<Contact> getContacts() {
        return contactList;
    }

    public void addContact(Contact contact) {
        if (contact != null) {
            contactList.add(contact);
        }
    }

    public void updateContact(int index, Contact contact) {
        if (index >= 0 && index < contactList.size() && contact != null) {
            contactList.set(index, contact);
        }
    }

    public void deleteContact(int index) {
        if (index >= 0 && index < contactList.size()) {
            contactList.remove(index);
        }
    }

    /**
     * Kiểm tra số điện thoại đã tồn tại trong danh bạ hay chưa
     * @param phone số điện thoại cần kiểm tra
     * @param excludeIndex vị trí bỏ qua khi kiểm tra (dùng khi cập nhật, truyền -1 khi thêm mới)
     */
    public boolean isPhoneExists(String phone, int excludeIndex) {
        if (phone == null) return false;
        String cleanPhone = phone.replaceAll("\\s+", "");
        for (int i = 0; i < contactList.size(); i++) {
            if (i == excludeIndex) continue;
            String existingPhone = contactList.get(i).getPhoneNumber().replaceAll("\\s+", "");
            if (existingPhone.equals(cleanPhone)) {
                return true;
            }
        }
        return false;
    }

    public int getTotalCount() {
        return contactList.size();
    }

    public int getFavoriteCount() {
        int count = 0;
        for (Contact c : contactList) {
            if (c.isFavorite()) {
                count++;
            }
        }
        return count;
    }
}
