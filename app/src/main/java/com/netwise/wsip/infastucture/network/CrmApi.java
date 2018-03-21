package com.netwise.wsip.infastucture.network;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CrmApi {

    @POST("GetAllData")
    Single<ResponseDto> getAllData(@Body CredentialsDto credentialsDto);
}
