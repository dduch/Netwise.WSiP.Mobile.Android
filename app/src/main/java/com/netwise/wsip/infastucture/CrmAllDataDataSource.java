package com.netwise.wsip.infastucture;

import android.support.annotation.NonNull;

import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.domain.crm.NoteHeader;
import com.netwise.wsip.domain.crm.School;
import com.netwise.wsip.domain.crm.Teacher;
import com.netwise.wsip.infastucture.network.CredentialsDto;
import com.netwise.wsip.infastucture.network.CrmApi;
import com.netwise.wsip.infastucture.network.ResponseDto;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class CrmAllDataDataSource {
    CrmApi api;
    CredentialsDto credentialsDto;

    @Inject
    public CrmAllDataDataSource(CrmApi api) {
        this.api = api;
        credentialsDto = new CredentialsDto();
    }

    public Single<CrmData> getAllData() {
        return api.getAllData(credentialsDto)
            .map(this::mapDto);
    }


    //mozna wyniesc do osobnej klasy mappera
    //ctrl + alt + m  ekstrakacja do metody
    @NonNull
    private CrmData mapDto(ResponseDto responseDto) {
       return ModelMapper.mapDto(responseDto);
    }
}
