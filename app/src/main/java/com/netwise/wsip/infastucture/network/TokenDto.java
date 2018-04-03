package com.netwise.wsip.infastucture.network;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import com.ryanharter.auto.value.gson.GenerateTypeAdapter;

import java.util.Date;

/**
 * Created by dawido on 23.03.2018.
 */
@AutoValue
public abstract class TokenDto {
    @SerializedName("access_token")
    public abstract String access_token();
    @SerializedName("token_type")
    public abstract String token_type();
    @SerializedName("expires_in")
    public abstract int expires_in();

    public Date ExpiringDateTime;

    public static TokenDto create(String access_token, String token_type, int expires_in,
                                  Date expiringDateTime) {
        return new AutoValue_TokenDto(access_token, token_type, expires_in);
    }

    public static TypeAdapter<TokenDto> typeAdapter(Gson gson) {
        return new AutoValue_TokenDto.GsonTypeAdapter(gson);
    }
}
