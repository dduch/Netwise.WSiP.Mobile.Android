package com.netwise.wsip.presentation.fake;

import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.injection.ViewModelKey;
import com.netwise.wsip.presentation.login.LoginModule;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class FakeModule {
    @Binds
    @IntoMap
    @ViewModelKey(FakeViewModel.class)
    abstract ViewModel bindFakeVM(FakeViewModel vm);

}
