package com.netwise.wsip.presentation.crm;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class CrmBuilder {
    @ContributesAndroidInjector(modules = {CrmModule.class})
    abstract CrmActivity crmActivityActivity();
}
