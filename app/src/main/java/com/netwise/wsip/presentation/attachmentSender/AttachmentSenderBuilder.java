package com.netwise.wsip.presentation.attachmentSender;

import com.netwise.wsip.presentation.fake.FakeActivity;
import com.netwise.wsip.presentation.fake.FakeModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by dawido on 15.03.2018.
 */

@Module
public abstract class AttachmentSenderBuilder {
    @ContributesAndroidInjector(modules = {FakeModule.class})
    abstract AttachmentSenderActivity attachmentSenderActivity();
}
