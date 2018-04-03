package com.netwise.wsip.infastucture.network;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dawido on 03.04.2018.
 */

@AutoValue
public abstract class NoteHeaderDto{
    @SerializedName("id")
    public abstract String Id();

    @SerializedName("name")
    public abstract String name();

    public static NoteHeaderDto create(String Id, String name) {
        return new AutoValue_NoteHeaderDto(Id, name);
    }

    public static TypeAdapter<NoteHeaderDto> typeAdapter(Gson gson) {
        return new AutoValue_NoteHeaderDto.GsonTypeAdapter(gson);
    }
}