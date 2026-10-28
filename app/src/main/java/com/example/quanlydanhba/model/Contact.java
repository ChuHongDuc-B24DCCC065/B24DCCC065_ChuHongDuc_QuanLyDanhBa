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
     * Trả về màu nền pastel nhẹ cho Avatar (theo chữ cái)
     */
    public int getAvatarBgColor() {
        String initial = getInitialLetter();
        if (initial.isEmpty()) {
            return Color.parseColor("#D1FAE5");
        }
        char c = initial.charAt(0);
        switch (c) {
            case 'A':
                return Color.parseColor("#D1FAE5"); // Xanh lục pastel nhẹ (An)
            case 'B':
                return Color.parseColor("#D1FAE5"); // Xanh lục pastel nhẹ (Bình)
            case 'C':
                return Color.parseColor("#EDE9FE"); // Tím pastel nhẹ (Châu)
            case 'D':
            case 'Đ':
                return Color.parseColor("#DBEAFE"); // Xanh dương pastel
            case 'H':
            case 'K':
                return Color.parseColor("#CCFBF1"); // Xanh ngọc pastel
            case 'L':
            case 'M':
                return Color.parseColor("#FCE7F3"); // Hồng pastel
            default:
                int hash = Math.abs(initial.hashCode()) % 4;
                int[] bgColors = {
                        Color.parseColor("#D1FAE5"),
                        Color.parseColor("#EDE9FE"),
                        Color.parseColor("#DBEAFE"),
                        Color.parseColor("#CCFBF1")
                };
                return bgColors[hash];
        }
    }

    /**
     * Trả về màu chữ đậm tương phản cho Avatar
     */
    public int getAvatarTextColor() {
        String initial = getInitialLetter();
        if (initial.isEmpty()) {
            return Color.parseColor("#059669");
        }
        char c = initial.charAt(0);
        switch (c) {
            case 'A':
            case 'B':
                return Color.parseColor("#059669"); // Xanh lục đậm
            case 'C':
                return Color.parseColor("#7C3AED"); // Tím đậm
            case 'D':
            case 'Đ':
                return Color.parseColor("#2563EB"); // Xanh dương đậm
            case 'H':
            case 'K':
                return Color.parseColor("#0D9488"); // Xanh ngọc đậm
            case 'L':
            case 'M':
                return Color.parseColor("#DB2777"); // Hồng đậm
            default:
                int hash = Math.abs(initial.hashCode()) % 4;
                int[] textColors = {
                        Color.parseColor("#059669"),
                        Color.parseColor("#7C3AED"),
                        Color.parseColor("#2563EB"),
                        Color.parseColor("#0D9488")
                };
                return textColors[hash];
        }
    }
}
