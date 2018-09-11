package com.example.ziv32.contactsmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Contact> contactsArrayList = null;
    static ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadContactList();

        contactAdapter = new ContactAdapter(contactsArrayList);
        recyclerView.setAdapter(contactAdapter);

        contactAdapter.setListener(new ContactAdapter.MyContactListener() {
            @Override
            public void onContactClicked(int position, View view) {
                Intent intent = new Intent(getApplicationContext(), ShowContactActivity.class);
                intent.putExtra("contact position", position);
                startActivity(intent);
            }

            @Override
            public void onContactLongClicked(final int position, View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete contact").setMessage("Do you want to delete contact?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                contactsArrayList.remove(position);
                                contactAdapter.notifyItemRemoved(position);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                String number = contactsArrayList.get(viewHolder.getAdapterPosition()).getPhoneNumber();

                if(!number.matches("^[+]?[0-9]{3,13}$")){
                    Toast.makeText(MainActivity.this, "Not a valid phone number", Toast.LENGTH_SHORT).show();
                    contactAdapter.notifyDataSetChanged();
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + number));
                startActivity(intent);

                contactAdapter.notifyDataSetChanged();
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_contact_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.add_contact_btn:
                startActivity(new Intent(this, AddContactDialogActivity.class));
                return true;
            case R.id.delete_all_contacts:
                contactsArrayList.clear();
                contactAdapter.notifyDataSetChanged();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        FileOutputStream fileOutputStream;
        ObjectOutputStream objectOutputStream = null;

        try {
            fileOutputStream = openFileOutput("contacts", Activity.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(contactsArrayList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(objectOutputStream != null) objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadContactList(){

        FileInputStream fileInputStream;
        ObjectInputStream objectInputStream = null;

        try {
            fileInputStream = openFileInput("contacts");
            objectInputStream = new ObjectInputStream(fileInputStream);
            contactsArrayList = (ArrayList<Contact>)objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                if(objectInputStream != null) objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(contactsArrayList==null) contactsArrayList = new ArrayList<>();
    }
}


