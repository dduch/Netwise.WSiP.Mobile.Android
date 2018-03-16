package com.netwise.wsip.injection;

import android.app.Application;

import com.netwise.wsip.WsipApp;
import com.netwise.wsip.injection.network.ApiModule;
import com.netwise.wsip.injection.network.NetworkModule;
import com.netwise.wsip.presentation.attachmentSender.AttachementSenderModule;
import com.netwise.wsip.presentation.attachmentSender.AttachmentSenderBuilder;
import com.netwise.wsip.presentation.crm.CrmBuilder;
import com.netwise.wsip.presentation.fake.FakeBuilder;
import com.netwise.wsip.presentation.login.LoginBuilder;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
    modules = {
        AndroidSupportInjectionModule.class,
        LoginBuilder.class,
        ViewModelModule.class,
        FakeBuilder.class,
        CrmBuilder.class,
        ApiModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        AttachementSenderModule.class,
        AttachmentSenderBuilder.class
    }
)
public interface AppComponent {

    @Component.Builder
    public interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }


    void inject(WsipApp app);
}
