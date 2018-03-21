package com.netwise.wsip.presentation.schoolTeachers;

import com.netwise.wsip.presentation.attachmentSender.AttachmentSenderActivity;
import com.netwise.wsip.presentation.crm.CrmModule;
import com.netwise.wsip.presentation.fake.FakeModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by dawido on 19.03.2018.
 */

@Module
public abstract class SchoolTeachersBuilder {
    @ContributesAndroidInjector(modules = {CrmModule.class})
    abstract SchoolTeachersActivity  schoolTeachersActivity ();
}
