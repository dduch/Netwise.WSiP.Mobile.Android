package com.netwise.wsip.injection.network;

import com.google.gson.GsonBuilder;
import com.netwise.wsip.BuildConfig;
import com.netwise.wsip.infastucture.network.AutoValueGsonFactory;
import com.netwise.wsip.infastucture.network.CrmApi;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    @Singleton
    @Provides
    CrmApi provideCrmApi(Retrofit retrofit) {
        return retrofit.create(CrmApi.class);
    }

    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Converter.Factory jsonFactory, CallAdapter.Factory callAdapterFactory) {
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder().registerTypeAdapterFactory(AutoValueGsonFactory.create())
                        .create());

        return new Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build();
    }

    @Singleton
    @Provides
    OkHttpClient provideHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);

        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }
}
