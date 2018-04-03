package com.netwise.wsip.domain.crm;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dawido on 27.03.2018.
 */

public class Attachment implements Parcelable {
    public String crmEntityName;
    public String id;
    public String fileName;
    public String subject;
    public String body;
    public String mimeType;
    public byte[] data;

    public Attachment(){
        this.mimeType = "image/jpeg";
    }

    protected Attachment(Parcel in) {
        crmEntityName = in.readString();
        id = in.readString();
        fileName = in.readString();
        subject = in.readString();
        body = in.readString();
        data = in.createByteArray();
        mimeType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(crmEntityName);
        dest.writeString(id);
        dest.writeString(fileName);
        dest.writeString(subject);
        dest.writeString(body);
        dest.writeByteArray(data);
        dest.writeString(mimeType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
}
