package com.netwise.wsip.infastucture;

import android.support.annotation.NonNull;

import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.infastucture.network.AttachmentRequestDto;
import com.netwise.wsip.infastucture.network.CredentialsDto;
import com.netwise.wsip.infastucture.network.CrmApi;
import com.netwise.wsip.infastucture.network.ResponseDto;
import com.netwise.wsip.infastucture.network.TokenDto;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

@Singleton
public class CrmAllDataDataSource {
    private static final long ONE_MIN_IN_MS = 60000;
    private static final int VALIDITY_TOKEN_BUFFER_IN_MINUTES = 1;
    private CrmApi api;
    private CredentialsDto credentialsDto;
    private TokenDto token;

    public void setCredentialsDto(CredentialsDto credentialsDto) {
        this.credentialsDto = credentialsDto;
        this.credentialsDto.grant_type = "password";
    }

    public CredentialsDto getCredentialsDto() {
        return this.credentialsDto;
    }

    @Inject
    public CrmAllDataDataSource(CrmApi api) {
        this.api = api;
        this.credentialsDto = new CredentialsDto();
        this.credentialsDto.userName = "";
        this.credentialsDto.password = "";
    }

    public Single<TokenDto> getToken(){
        return this.api.getToken(credentialsDto.grant_type, credentialsDto.userName, credentialsDto.password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(TokenDto -> TokenDto);
    }

    public Single<CrmData> getAllData() {
        if(token == null ||  new Date(token.expires_in() + VALIDITY_TOKEN_BUFFER_IN_MINUTES * ONE_MIN_IN_MS).after(new Date())){
            return api.getToken(credentialsDto.grant_type, credentialsDto.userName, credentialsDto.password)
                    .flatMap(t -> api.getAllData("Bearer " + t.access_token())
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(this::mapDto);
        }else{
            return api.getAllData("Bearer " + token.access_token()).map(this::mapDto);
        }
    }

    public Single<ResponseBody> uploadAttachment(AttachmentRequestDto attachmentData){
        if(token == null ||  new Date(token.expires_in() + VALIDITY_TOKEN_BUFFER_IN_MINUTES * ONE_MIN_IN_MS).after(new Date())){
            return api.getToken(credentialsDto.grant_type, credentialsDto.userName, credentialsDto.password)
                    .flatMap(t -> api.uploadAttachment("Bearer " + t.access_token(), "application/json; charset=utf-8", attachmentData))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

        }else{
            return api.uploadAttachment("Bearer " + token.access_token(),"application/json; charset=utf-8", attachmentData);
        }
    }



    //mozna wyniesc do osobnej klasy mappera
    //ctrl + alt + m  ekstrakacja do metody
    @NonNull
    private CrmData mapDto(ResponseDto responseDto) {
       return ModelMapper.mapDto(responseDto);
    }

    public void setToken(TokenDto token) {
        this.token = token;
    }
}
