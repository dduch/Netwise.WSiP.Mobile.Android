package com.netwise.wsip.presentation.attachmentSender;

import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.injection.ViewModelKey;
import com.netwise.wsip.presentation.fake.FakeViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by dawido on 15.03.2018.
 */

@Module
public abstract class AttachementSenderModule {
    @Binds
    @IntoMap
    @ViewModelKey(AttachementSenderViewModel.class)
    abstract ViewModel bindAttachmentSenderVM(AttachementSenderViewModel vm);
}
