package com.example.ziv32.contactsmanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowContactActivity extends AppCompatActivity {

    final int CAMERA_REQUEST_CODE = 1;

    private int contactPosition;

    TextView mFullNameEditText;
    TextView mPhoneNumberEditText;
    TextView mEmailAddressEditText;
    TextView mHomeAddressEditText;
    TextView mWebsiteEditText;
    TextView mDateOfBirthTextView;
    TextView mTimeToCallTextView;
    TextView mDaysToCallTextView;

    ImageView mContactPictureIm;

    String numberToCall, mailTo, addressForMap, addressForWebsite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);
        setResources();

        Intent intent = getIntent();
        contactPosition = intent.getIntExtra("contact position", 0);
        Contact contact = MainActivity.contactsArrayList.get(contactPosition);

        mFullNameEditText.setText(contact.getName());
        mPhoneNumberEditText.setText(contact.getPhoneNumber());
        mEmailAddressEditText.setText(contact.getEmailAddress());
        mHomeAddressEditText.setText(contact.getHomeAddress());
        mWebsiteEditText.setText(contact.getWebsite());
        mDateOfBirthTextView.setText(contact.getDateOfBirth());
        mTimeToCallTextView.setText(contact.getTimeToCall());
        mDaysToCallTextView.setText(contact.getDaysToCall());
        mContactPictureIm.setImageBitmap(contact.getContactPhoto());

        numberToCall = mPhoneNumberEditText.getText().toString();
        if(numberToCall.matches("^[+]?[0-9]{3,13}$")){
            mPhoneNumberEditText.setTextColor(Color.BLUE);
        }

        mailTo = mEmailAddressEditText.getText().toString();
        if(!mailTo.equals("---")) mEmailAddressEditText.setTextColor(Color.BLUE);

        addressForMap = mHomeAddressEditText.getText().toString();
        if(!addressForMap.equals("---")) mHomeAddressEditText.setTextColor(Color.BLUE);

        addressForWebsite = mWebsiteEditText.getText().toString();
        if(!addressForWebsite.equals("---")) mWebsiteEditText.setTextColor(Color.BLUE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            MainActivity.contactsArrayList.get(contactPosition).setmContactPhoto(bitmap);
            mContactPictureIm.setImageBitmap(bitmap);
            MainActivity.contactAdapter.notifyDataSetChanged();
        }
    }

    private void setResources(){
        mFullNameEditText = (TextView)findViewById(R.id.show_contact_full_name);
        mPhoneNumberEditText = (TextView)findViewById(R.id.show_contact_phone_number);
        mEmailAddressEditText = (TextView)findViewById(R.id.show_contact_email_address);
        mHomeAddressEditText = (TextView)findViewById(R.id.show_contact_home_address);
        mWebsiteEditText = (TextView)findViewById(R.id.show_contact_website);
        mDateOfBirthTextView = (TextView)findViewById(R.id.show_contact_birthday);
        mTimeToCallTextView = (TextView)findViewById(R.id.show_contact_call_time);
        mDaysToCallTextView = (TextView)findViewById(R.id.show_contact_call_days);
        mContactPictureIm = (ImageView) findViewById(R.id.show_contact_photo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.show_contact_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_btn:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void callContact(View view){
        if(!numberToCall.matches("^[+]?[0-9]{3,13}$")){
            Toast.makeText(this, "Not a valid phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + numberToCall));
        startActivity(intent);
    }

    public void mailContact(View view){
        if(mailTo.equals("---")) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { mailTo });
        intent.setType("text/html");
        startActivity(intent);
    }

    public void openAddressInMaps(View view){
        if(addressForMap.equals("---")) return;

        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(mHomeAddressEditText.getText().toString()));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void openWebsiteLink(View view){
        if(addressForWebsite.equals("---")) return;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + mWebsiteEditText.getText().toString()));
        startActivity(intent);
    }

    public void replacePhoto(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
}
