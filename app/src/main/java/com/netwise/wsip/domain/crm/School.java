package com.netwise.wsip.domain.crm;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dawido on 13.03.2018.
 */

public class School implements Parcelable {
    public String name;
    public String schoolTypeName;
    public String street1;
    public String street2;
    public String postalCode;
    public String city;
    public String provinceName;
    public String itemId;
    public List<Teacher> teachers;

    public School(){}


    protected School(Parcel in) {
        name = in.readString();
        schoolTypeName = in.readString();
        street1 = in.readString();
        street2 = in.readString();
        postalCode = in.readString();
        city = in.readString();
        provinceName = in.readString();
        itemId = in.readString();
        teachers = in.createTypedArrayList(Teacher.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(schoolTypeName);
        dest.writeString(street1);
        dest.writeString(street2);
        dest.writeString(postalCode);
        dest.writeString(city);
        dest.writeString(provinceName);
        dest.writeString(itemId);
        dest.writeTypedList(teachers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<School> CREATOR = new Creator<School>() {
        @Override
        public School createFromParcel(Parcel in) {
            return new School(in);
        }

        @Override
        public School[] newArray(int size) {
            return new School[size];
        }
    };
}
