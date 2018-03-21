package com.netwise.wsip.presentation.crm;

import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.domain.crm.CrmRepository;
import com.netwise.wsip.infastucture.RemoteCrmDataRepository;
import com.netwise.wsip.injection.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CrmModule {
    @Binds
    @IntoMap
    @ViewModelKey(CrmViewModel.class)
    abstract ViewModel bindFakeVM(CrmViewModel vm);

}
