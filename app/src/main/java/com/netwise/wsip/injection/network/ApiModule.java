package com.netwise.wsip.injection.network;

import com.netwise.wsip.BuildConfig;
import com.netwise.wsip.infastucture.network.CrmApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

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
        return new Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build();
    }

    @Singleton
    @Provides
    OkHttpClient provideHttpClient(HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }
}
