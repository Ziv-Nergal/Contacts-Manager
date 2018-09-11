package com.example.ziv32.contactsmanager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Contact implements Serializable{

    private String mName;
    private String mPhoneNumber;
    private String mEmailAddress;
    private String mHomeAddress;
    private String mWebsite;
    private String mDateOfBirth;
    private String mTimeToCall;
    private String mDaysToCall;
    transient private Bitmap mContactPhoto;

    Contact(String mName, String mPhoneNumber, String mEmailAddress, String mHomeAddress,
                   String mWebsite, String mDateOfBirth, String mTimeToCall, String mDaysToCall, Bitmap mContactPhoto) {
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmailAddress = mEmailAddress;
        this.mHomeAddress = mHomeAddress;
        this.mWebsite = mWebsite;
        this.mDateOfBirth = mDateOfBirth;
        this.mTimeToCall = mTimeToCall;
        this.mDaysToCall = mDaysToCall;
        this.mContactPhoto = mContactPhoto;
    }

    Contact(Context context, String mName, String mPhoneNumber, String mEmailAddress, String mHomeAddress,
            String mWebsite, String mDateOfBirth, String mTimeToCall, String mDaysToCall) {
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mEmailAddress = mEmailAddress;
        this.mHomeAddress = mHomeAddress;
        this.mWebsite = mWebsite;
        this.mDateOfBirth = mDateOfBirth;
        this.mTimeToCall = mTimeToCall;
        this.mDaysToCall = mDaysToCall;
        this.mContactPhoto = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank_profile);
    }

    public String getName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setmEmailAddress(String mEmailAddress) {
        this.mEmailAddress = mEmailAddress;
    }

    public String getHomeAddress() {
        return mHomeAddress;
    }

    public void setmHomeAddress(String mHomeAddress) {
        this.mHomeAddress = mHomeAddress;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setmWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }

    public String getDateOfBirth() {
        return mDateOfBirth;
    }

    public void setmDateOfBirth(String mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }

    public String getTimeToCall() {
        return mTimeToCall;
    }

    public void setmTimeToCall(String mTimeToCall) {
        this.mTimeToCall = mTimeToCall;
    }

    public String getDaysToCall() {
        return mDaysToCall;
    }

    public void setmDaysToCall(String mDaysToCall) {
        this.mDaysToCall = mDaysToCall;
    }

    public Bitmap getContactPhoto() {
        return mContactPhoto;
    }

    public void setmContactPhoto(Bitmap mContactPhoto) {
        this.mContactPhoto = mContactPhoto;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        mContactPhoto.compress(Bitmap.CompressFormat.JPEG,100,oos);
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException,IOException {
        mContactPhoto = BitmapFactory.decodeStream(ois);
        ois.defaultReadObject();
    }
}