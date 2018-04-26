package com.netwise.wsip.infastucture;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.netwise.wsip.WsipApp;
import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.domain.crm.CrmRepository;
import com.netwise.wsip.domain.crm.NoteHeader;
import com.netwise.wsip.infastucture.network.AttachmentRequestDto;
import com.netwise.wsip.infastucture.network.CredentialsDto;
import com.netwise.wsip.infastucture.network.TokenDto;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;

@Singleton
public class RemoteCrmDataRepository implements CrmRepository {
    private static String DATA_PREF="DATA_PREF";
    private static String USER_DATA = "USER_DATA";
    private boolean areCredentialsChanged = false;
    CrmAllDataDataSource dataDataSource;
    CrmData cacheCrmData;
    TokenDto cachedToken;
    CredentialsDto credentialsDto;
    byte[] imageData;
    SharedPreferences mPrefs;


    @Inject
    public RemoteCrmDataRepository(CrmAllDataDataSource dataDataSource) {
        this.dataDataSource = dataDataSource;
        mPrefs = WsipApp.getAppContext().getSharedPreferences(DATA_PREF, MODE_PRIVATE);
    }


    @Override
    public Single<TokenDto> getToken() {
        if(cachedToken != null && cachedToken.access_token() != null){
            return Single.just(cachedToken);
        }
        return dataDataSource.getToken()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(token -> {
                    this.cachedToken = token;
                    dataDataSource.setToken(token);
                });
    }

    @Override
    public Single<CrmData> getCrmData() {
        if (cacheCrmData != null) {
            return Single.just(cacheCrmData);
        }

        return dataDataSource
            .getAllData()
            .subscribeOn(Schedulers.io())
            .doOnSuccess(crmData -> hendleReceivedData(crmData) );
    }

    @Override
    public Single<CrmData> refreshData() {
        return dataDataSource
                .getAllData()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(crmData -> hendleReceivedData(crmData));
    }


    private void hendleReceivedData(CrmData crmData){
       /* SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(crmData);
        prefsEditor.putString(USER_DATA, json);
        prefsEditor.commit();*/
       cacheCrmData = crmData;
    }

    @Override
    public Single<ResponseBody> uploadAttachment(AttachmentRequestDto attachmentData) {
        return dataDataSource.uploadAttachment(attachmentData);
    }

    @Override
    public void SetCredentials(CredentialsDto cred) {
        CredentialsDto storedCred = dataDataSource.getCredentialsDto();
        if(storedCred != null && storedCred.userName.equals(cred.userName) && storedCred.password.equals(cred.password)){
            this.areCredentialsChanged = false;
            return;
        }
        else{
            this.areCredentialsChanged = true;
            dataDataSource.setCredentialsDto(cred);
        }
    }

    @Override
    public List<NoteHeader> getNoteHeaders() {
        return cacheCrmData.getNotes();
    }

    @Override
    public void putImageData(byte[] image) {
        this.imageData = image;
    }

    @Override
    public byte[] getImageData() {
        return this.imageData;
    }
}
