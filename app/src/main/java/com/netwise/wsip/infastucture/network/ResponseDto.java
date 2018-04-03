package com.netwise.wsip.infastucture.network;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.TypeAdapter;
import com.ryanharter.auto.value.gson.GenerateTypeAdapter;

import java.util.Date;
import java.util.List;

@AutoValue
public abstract class ResponseDto {
    @SerializedName("userSchools")
    public abstract List<SchoolDto> schools();

    @SerializedName("noteHeaders")
    public abstract List<NoteHeaderDto> notes();

    public static ResponseDto create(List<SchoolDto> schools, List<NoteHeaderDto> notes) {
        return new AutoValue_ResponseDto(schools, notes);
    }

    public static TypeAdapter<ResponseDto> typeAdapter(Gson gson) {
        return new AutoValue_ResponseDto.GsonTypeAdapter(gson);
    }
}








