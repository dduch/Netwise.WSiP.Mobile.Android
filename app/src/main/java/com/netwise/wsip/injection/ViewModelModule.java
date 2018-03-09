package com.netwise.wsip.injection;

import android.arch.lifecycle.ViewModelProvider;

import com.netwise.wsip.utils.WsipViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelProviderfactory(WsipViewModelFactory factory);

}
