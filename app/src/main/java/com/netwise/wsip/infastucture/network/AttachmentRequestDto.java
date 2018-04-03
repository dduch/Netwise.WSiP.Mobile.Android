package com.netwise.wsip.infastucture.network;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dawido on 28.03.2018.
 */

public class AttachmentRequestDto {
    @SerializedName("RegardingObjectIdEntityName")
    public String crmEntityName;
    @SerializedName("RegardingObjectId")
    public String id;
    @SerializedName("Filename")
    public String fileName;
    @SerializedName("Subject")
    public String subject;
    @SerializedName("Body")
    public String body;
    @SerializedName("MimeType")
    public String mimeType;
    @SerializedName("Data")
    public String data;
}
