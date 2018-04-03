package com.netwise.wsip;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import com.netwise.wsip.injection.AppComponent;
import com.netwise.wsip.injection.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class WsipApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    private AppComponent appComponent;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build();

        appComponent.inject(this);
        WsipApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return WsipApp.context;
    }
    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}

//todo
/**
 *  SharedPreferences jako storage
**/