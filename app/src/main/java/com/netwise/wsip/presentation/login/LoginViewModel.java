package com.netwise.wsip.presentation.login;

import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.domain.crm.CrmRepository;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class LoginViewModel extends ViewModel {

    CrmRepository repository;
    Disposable dis;

    @Inject
    public LoginViewModel(CrmRepository repository) {
        this.repository = repository;
        loadData();
    }

    private void loadData() {
        dis = repository.getCrmData().subscribe();
    }

    void clear() {
        dis.dispose();
    }
}
