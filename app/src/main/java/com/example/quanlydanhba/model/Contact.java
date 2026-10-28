package com.example.quanlydanhba.model;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Lớp biểu diễn một liên hệ trong danh bạ:
 * Họ tên, số điện thoại, nhóm, giới tính, yêu thích, ghi chú.
 */
public class Contact implements Serializable {
    private String name;
    private String phoneNumber;
    private String group;
    private String gender;
    private boolean isFavorite;
    private String note;

    public Contact() {
    }

    public Contact(String name, String phoneNumber, String group, String gender, boolean isFavorite, String note) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.group = group;
        this.gender = gender;
        this.isFavorite = isFavorite;
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Lấy chữ cái đầu của tên (theo tên gọi tiếng Việt):
     * Ví dụ: "Nguyễn Văn An" -> "A", "Trần Thị Bình" -> "B", "Lê Minh Châu" -> "C"
     */
    public String getInitialLetter() {
        if (name == null || name.trim().isEmpty()) {
            return "?";
        }
        String trimmed = name.trim();
        String[] parts = trimmed.split("\\s+");
        String lastPart = parts[parts.length - 1];
        if (!lastPart.isEmpty()) {
            return String.valueOf(lastPart.charAt(0)).toUpperCase();
        }
        return String.valueOf(trimmed.charAt(0)).toUpperCase();
    }

    /**
     * Trả về màu sắc đại diện cho chữ cái đầu của tên
     */
    public int getAvatarColor() {
        String initial = getInitialLetter();
        if (initial.isEmpty()) {
            return Color.parseColor("#00796B");
        }
        char c = initial.charAt(0);
        switch (c) {
            case 'A':
                return Color.parseColor("#00796B"); // Xanh teal đậm (như An trong Hình 4)
            case 'B':
                return Color.parseColor("#D84315"); // Cam đỏ (như Bình trong Hình 4)
            case 'C':
                return Color.parseColor("#5E35B1"); // Tím đậm (như Châu trong Hình 4)
            case 'D':
            case 'Đ':
                return Color.parseColor("#1565C0"); // Xanh dương
            case 'H':
            case 'K':
                return Color.parseColor("#2E7D32"); // Xanh lá
            case 'L':
            case 'M':
                return Color.parseColor("#C2185B"); // Hồng sen
            case 'N':
            case 'P':
                return Color.parseColor("#00838F"); // Xanh lơ
            case 'T':
            case 'V':
                return Color.parseColor("#E65100"); // Cam cháy
            default:
                int hash = Math.abs(initial.hashCode()) % 5;
                int[] fallbackColors = {
                        Color.parseColor("#00796B"),
                        Color.parseColor("#D84315"),
                        Color.parseColor("#5E35B1"),
                        Color.parseColor("#1565C0"),
                        Color.parseColor("#2E7D32")
                };
                return fallbackColors[hash];
        }
    }
}
