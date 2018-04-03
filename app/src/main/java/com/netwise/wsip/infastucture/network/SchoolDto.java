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
public abstract class SchoolDto {
    @SerializedName("id")
    public abstract String Id();

    @SerializedName("name")
    @Nullable
    public abstract String Name();

    @SerializedName("schoolType")
    @Nullable
    public String SchoolType;

    @SerializedName("schoolTypeName")
    @Nullable
    public abstract String SchoolTypeName();

    @SerializedName("street1")
    @Nullable
    public abstract String Street1();

    @SerializedName("street2")
    @Nullable
    public abstract String Street2();

    @SerializedName("postalCodeName")
    @Nullable
    public abstract String PostalCodeName();

    @SerializedName("city")
    @Nullable
    public abstract String City();

    @SerializedName("provinceName")
    @Nullable
    public abstract String ProvinceName();

    @SerializedName("teachers")
    @Nullable
    public abstract List<TeacherDto> Teachers();

    public static SchoolDto create(String Id, String Name, String SchoolTypeName, String Street1, String Street2,
                                   String PostalCodeName, String City, String ProvinceName, List<TeacherDto> Teachers) {
        return new AutoValue_SchoolDto(Id, Name, SchoolTypeName, Street1, Street2, PostalCodeName, City, ProvinceName, Teachers);
    }

    public static TypeAdapter<SchoolDto> typeAdapter(Gson gson) {
        return new AutoValue_SchoolDto.GsonTypeAdapter(gson);
    }
}