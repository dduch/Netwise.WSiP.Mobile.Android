package com.netwise.wsip.presentation.login;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.domain.crm.CrmRepository;

import org.reactivestreams.Subscriber;

import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class LoginViewModel extends ViewModel {

    public CrmRepository repository;
    Disposable dis;

    @Inject
    public LoginViewModel(CrmRepository repository) {
        this.repository = repository;
    }

    public void loadData() {
        dis = repository.getCrmData().subscribe();
    }

    public void getToken(){
        repository.getToken().subscribe();
    }


    void clear() {
        dis.dispose();
    }
}
