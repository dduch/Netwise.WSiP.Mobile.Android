package com.netwise.wsip.infastucture.network;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dawido on 03.04.2018.
 */

@AutoValue
public abstract class TeacherDto {
    @SerializedName("id")
    public abstract String Id();

    @SerializedName("fullName")
    @Nullable
    public abstract String FullName();

    @SerializedName("email")
    @Nullable
    public abstract String Email();

    @SerializedName("mobilePhone")
    @Nullable
    public abstract String MobilePhone();

    public static TeacherDto create(String Id, String FullName, String Email, String MobilePhone) {
        return new AutoValue_TeacherDto(Id, FullName, Email, MobilePhone);
    }
    public static TypeAdapter<TeacherDto> typeAdapter(Gson gson) {
        return new AutoValue_TeacherDto.GsonTypeAdapter(gson);
    }
}