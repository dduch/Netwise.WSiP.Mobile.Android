package com.netwise.wsip.presentation.schoolTeachers;

import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.injection.ViewModelKey;
import com.netwise.wsip.presentation.crm.CrmViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by dawido on 19.03.2018.
 */

@Module
public abstract class SchoolTeachersModule {
    @Binds
    @IntoMap
    @ViewModelKey(SchoolTeachersViewModel.class)
    abstract ViewModel bindSchoolTeachersVM(SchoolTeachersViewModel vm);
}
