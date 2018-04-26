package com.netwise.wsip.infastucture.network;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface CrmApi {

    @GET("api/Mobile/GetAllData")
    Single<ResponseDto> getAllData(@Header("Authorization") String authHeader);

    @POST("api/Mobile/Upload")
    Single<ResponseBody> uploadAttachment(@Header("Authorization") String authHeader, @Header("Content-Type") String contentType, @Body AttachmentRequestDto attachmentDto);

    @FormUrlEncoded
    @POST("token")
    Single<TokenDto> getToken(@Field("grant_type") String grantType, @Field("username") String userName, @Field("password") String password);
}
