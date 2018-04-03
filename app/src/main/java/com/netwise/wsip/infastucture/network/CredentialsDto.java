package com.netwise.wsip.infastucture.network;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.ryanharter.auto.value.gson.GenerateTypeAdapter;

@AutoValue
public class CredentialsDto {

    @SerializedName("UserName")
    public String userName;
    @SerializedName("Password")
    public String password;
    @SerializedName("grant_type")
    public String grant_type;

    public static TypeAdapter<CredentialsDto> typeAdapter(Gson gson) {
        return new AutoValue_CredentialsDto.GsonTypeAdapter(gson);
    }
}
