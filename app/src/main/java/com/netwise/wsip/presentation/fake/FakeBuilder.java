package com.netwise.wsip.presentation.fake;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FakeBuilder {
    @ContributesAndroidInjector(modules = {FakeModule.class})
    abstract FakeActivity fakeActivity();
}
