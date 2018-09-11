package com.example.ziv32.contactsmanager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private MyContactListener listener;

    ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    interface MyContactListener {
        void onContactClicked(int position, View view);
        void onContactLongClicked(int position, View view);
    }

    public void setListener(MyContactListener listener) {
        this.listener = listener;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        CircleImageView mUserPhoto;
        TextView mFullNameTv;
        TextView mPhoneNumberTv;

        ContactViewHolder(View itemView) {
            super(itemView);
            mUserPhoto = (CircleImageView)itemView.findViewById(R.id.contact_photo);
            mFullNameTv = (TextView)itemView.findViewById(R.id.name_text_view);
            mPhoneNumberTv = (TextView)itemView.findViewById(R.id.phone_number_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onContactClicked(getAdapterPosition(), v);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onContactLongClicked(getAdapterPosition(), v);
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_cell, parent,false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.mUserPhoto.setImageBitmap(contact.getContactPhoto());
        holder.mFullNameTv.setText(contact.getName());
        holder.mPhoneNumberTv.setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            return contacts.size();
        }
        return 0;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
