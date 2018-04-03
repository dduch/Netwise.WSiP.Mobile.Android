package com.netwise.wsip.domain.crm;

import com.netwise.wsip.infastucture.network.AttachmentRequestDto;
import com.netwise.wsip.infastucture.network.CredentialsDto;
import com.netwise.wsip.infastucture.network.TokenDto;

import java.util.List;

import io.reactivex.Single;
import okhttp3.Response;
import okhttp3.ResponseBody;

public interface CrmRepository {
    Single<TokenDto> getToken();

    Single<CrmData> getCrmData();

    Single<ResponseBody> uploadAttachment(AttachmentRequestDto attachmentData);

    void SetCredentials(CredentialsDto cred);

    List<NoteHeader> getNoteHeaders();

    void putImageData(byte[] image);

    byte[] getImageData();
}
