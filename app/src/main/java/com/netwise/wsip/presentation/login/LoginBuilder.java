package com.netwise.wsip.presentation.login;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoginBuilder {
    @ContributesAndroidInjector(modules = {LoginModule.class})
    abstract LoginActivity loginActivity();
}
