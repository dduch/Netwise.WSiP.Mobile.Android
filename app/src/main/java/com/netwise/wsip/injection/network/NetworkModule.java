package com.netwise.wsip.injection.network;

import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {
    @Singleton
    @Provides
    HttpLoggingInterceptor privideLoggingInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("LoggerInterceptor", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    @Provides
    @Singleton
    Converter.Factory provideJsonConverterFacgtory() {
        return GsonConverterFactory.create();
    }

    @Singleton
    @Provides
    CallAdapter.Factory provideCAF() {
        return RxJava2CallAdapterFactory.create();
    }

}
