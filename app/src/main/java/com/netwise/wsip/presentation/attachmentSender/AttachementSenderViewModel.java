package com.netwise.wsip.presentation.attachmentSender;

import android.arch.lifecycle.ViewModel;
import android.provider.ContactsContract;

import com.netwise.wsip.domain.crm.CrmRepository;
import com.netwise.wsip.domain.crm.NoteHeader;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by dawido on 15.03.2018.
 */

public class AttachementSenderViewModel extends ViewModel {
    private CrmRepository crmRepository;

    @Inject
    public AttachementSenderViewModel(CrmRepository repository) {
        this.crmRepository = repository;
    }

    public CrmRepository getCrmRepository() {
        return crmRepository;
    }

    public byte[] loadImage(){
        return this.crmRepository.getImageData();
    }
}