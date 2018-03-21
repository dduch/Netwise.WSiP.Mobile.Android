package com.netwise.wsip.domain.crm;

import android.os.Parcel;
import android.os.Parcelable;

import com.netwise.wsip.infastucture.network.ResponseDto;

import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class Teacher implements Parcelable {
    public String firstName;
    public String lastName;
    public String mainSchoolName;
    public String street1;
    public String street2;
    public String city;
    public String postalCodeName;
    public String provinceName;
    public String itemId;
    public String email;
    public String mobilePhone;

    public Teacher(){}

    protected Teacher(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        mainSchoolName = in.readString();
        street1 = in.readString();
        street2 = in.readString();
        city = in.readString();
        postalCodeName = in.readString();
        provinceName = in.readString();
        itemId = in.readString();
        email = in.readString();
        mobilePhone = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(mainSchoolName);
        dest.writeString(street1);
        dest.writeString(street2);
        dest.writeString(city);
        dest.writeString(postalCodeName);
        dest.writeString(provinceName);
        dest.writeString(itemId);
        dest.writeString(email);
        dest.writeString(mobilePhone);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };
}