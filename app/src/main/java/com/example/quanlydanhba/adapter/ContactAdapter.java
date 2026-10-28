package com.example.quanlydanhba.adapter;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydanhba.R;
import com.example.quanlydanhba.model.Contact;

import java.util.ArrayList;

/**
 * Adapter cho RecyclerView hiển thị danh sách các liên hệ.
 * Mỗi dòng hiển thị: chữ cái đầu của tên (ảnh đại diện tròn), họ tên, số điện thoại, nhóm và dấu sao yêu thích.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    public interface OnContactClickListener {
        void onContactClick(int position, Contact contact);
    }

    private final ArrayList<Contact> contactList;
    private final OnContactClickListener listener;

    public ContactAdapter(ArrayList<Contact> contactList, OnContactClickListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        // Họ và tên
        holder.tvContactName.setText(contact.getName());

        // Số điện thoại
        holder.tvContactPhone.setText(contact.getPhoneNumber());

        // Nhóm
        holder.tvContactGroup.setText(contact.getGroup());

        // Avatar tròn với chữ cái đầu
        holder.tvAvatarInitial.setText(contact.getInitialLetter());
        GradientDrawable avatarDrawable = new GradientDrawable();
        avatarDrawable.setShape(GradientDrawable.OVAL);
        avatarDrawable.setColor(contact.getAvatarColor());
        holder.layoutAvatar.setBackground(avatarDrawable);

        // Dấu sao vàng yêu thích
        if (contact.isFavorite()) {
            holder.ivFavoriteStar.setVisibility(View.VISIBLE);
        } else {
            holder.ivFavoriteStar.setVisibility(View.GONE);
        }

        // Bắt sự kiện nhấn vào một dòng liên hệ
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int currentPos = holder.getAdapterPosition();
                if (currentPos != RecyclerView.NO_POSITION) {
                    listener.onContactClick(currentPos, contactList.get(currentPos));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList != null ? contactList.size() : 0;
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        FrameLayout layoutAvatar;
        TextView tvAvatarInitial;
        TextView tvContactName;
        TextView tvContactPhone;
        TextView tvContactGroup;
        ImageView ivFavoriteStar;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutAvatar = itemView.findViewById(R.id.layoutAvatar);
            tvAvatarInitial = itemView.findViewById(R.id.tvAvatarInitial);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvContactPhone = itemView.findViewById(R.id.tvContactPhone);
            tvContactGroup = itemView.findViewById(R.id.tvContactGroup);
            ivFavoriteStar = itemView.findViewById(R.id.ivFavoriteStar);
        }
    }
}
