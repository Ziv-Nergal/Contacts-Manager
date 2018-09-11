package com.example.ziv32.contactsmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactDialogActivity extends AppCompatActivity {

    String emailAddress;
    String homeAddress;
    String website;
    String dateOfBirth;
    String timeToCall;
    String daysToCall;
    ArrayList<String> mDaysToCall;

    final int CAMERA_REQUEST_CODE = 1;

    EditText mFullNameEditText;
    EditText mPhoneNumberEditText;
    EditText mEmailAddressEditText;
    EditText mHomeAddressEditText;
    EditText mWebsiteEditText;

    TextView mDateOfBirthTextView;
    TextView mTimeToCallTextView;
    TextView mDaysToCallTextView;

    CircleImageView mContactPictureIm;

    Bitmap usersPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_contact_dialog);
        this.setFinishOnTouchOutside(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            usersPhoto = (Bitmap) data.getExtras().get("data");
            mContactPictureIm = (CircleImageView)findViewById(R.id.contact_picture_iv);
            mContactPictureIm.setImageBitmap(usersPhoto);
        }
    }
    public void onDateOfBirthClick(View view){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateOfBirth =  dayOfMonth + "." + (month + 1) + "." + year;
                mDateOfBirthTextView = (TextView)findViewById(R.id.date_of_birth_tv);
                mDateOfBirthTextView.setText(getResources().getString(R.string.birthday).concat(" " + dateOfBirth));
            }
        }, year, month, day);

        datePickerDialog.show();
    }
    public  void onTimeToCallClick(View view){

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeToCall = hourOfDay + ":" + minute;
                mTimeToCallTextView = (TextView)findViewById(R.id.call_time_tv);
                mTimeToCallTextView.setText(getResources().getString(R.string.call_time).concat(" " + timeToCall));
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }
    public void onDaysToCallClick(View view){

        final String[] daysOfWeek = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        mDaysToCall = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose days to call").setMultiChoiceItems(daysOfWeek, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    mDaysToCall.add(daysOfWeek[which]);
                } else if (mDaysToCall.contains(daysOfWeek[which])) {
                    mDaysToCall.remove(daysOfWeek[which]);
                }
            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDaysToCallTextView = (TextView)findViewById(R.id.call_days_tv);
                mDaysToCallTextView.setText(getResources().getString(R.string.call_days).concat(" " + mDaysToCall.toString()));
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
    public void onTakePhotoClick(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
    public void onSaveClick(View view){

        Contact contact;
        validateContactDetails();

        mFullNameEditText = (EditText)findViewById(R.id.name_edit_text);
        mPhoneNumberEditText = (EditText)findViewById(R.id.phone_number_edit_text);

        if(mFullNameEditText.getText().toString().trim().equals("")
                || mPhoneNumberEditText.getText().toString().trim().equals("")){
            Toast.makeText(this, "Please enter full name & phone number", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(usersPhoto == null) {
            contact = new Contact(this, mFullNameEditText.getText().toString(), mPhoneNumberEditText.getText().toString(), emailAddress,
                    homeAddress, website, dateOfBirth, timeToCall, daysToCall);
        }else{
            contact = new Contact(mFullNameEditText.getText().toString(), mPhoneNumberEditText.getText().toString(), emailAddress,
                    homeAddress, website, dateOfBirth, timeToCall, daysToCall, usersPhoto);
        }

        if(MainActivity.contactsArrayList == null) {
            MainActivity.contactsArrayList = new ArrayList<>();
        }
        MainActivity.contactsArrayList.add(contact);
        Collections.sort(MainActivity.contactsArrayList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        MainActivity.contactAdapter.setContacts(MainActivity.contactsArrayList);
        MainActivity.contactAdapter.notifyDataSetChanged();
        finish();
    }
    public void validateContactDetails(){

        mEmailAddressEditText = (EditText)findViewById(R.id.email_edit_text);
        mHomeAddressEditText = (EditText)findViewById(R.id.address_edit_text);
        mWebsiteEditText = (EditText)findViewById(R.id.website_edit_text);

        if(mEmailAddressEditText != null && !mEmailAddressEditText.getText().toString().equals("")){
            emailAddress = mEmailAddressEditText.getText().toString();
        }else {
            emailAddress = "---";
        }

        if(mHomeAddressEditText != null && !mHomeAddressEditText.getText().toString().equals("")){
            homeAddress = mHomeAddressEditText.getText().toString();
        }else {
        homeAddress = "---";
        }

        if(mWebsiteEditText != null &&  !mWebsiteEditText.getText().toString().equals("")){
            website = mWebsiteEditText.getText().toString();
        }else {
            website = "---";
        }

        if(mDateOfBirthTextView == null || mDateOfBirthTextView.getText().toString().equals("")) {
            dateOfBirth = "---";
        }

        if(mTimeToCallTextView == null || mTimeToCallTextView.getText().toString().equals("")) {
            timeToCall = "---";
        }

        if(mDaysToCall != null && !mDaysToCall.toString().equals("[]")){
            daysToCall = mDaysToCall.toString();
        }else {
            daysToCall = "---";
        }
    }
}

