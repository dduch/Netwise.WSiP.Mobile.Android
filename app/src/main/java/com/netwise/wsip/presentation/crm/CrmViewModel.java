package com.netwise.wsip.presentation.crm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.domain.crm.CrmRepository;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class CrmViewModel extends ViewModel {
    private CrmRepository crmRepository;
    private MutableLiveData<CrmViewState> state = new MutableLiveData<>();
    private CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public CrmViewModel(CrmRepository repository) {
        this.crmRepository = repository;
        state.setValue(CrmViewState.builder().build());
        loadCrmData();
    }

    private void loadCrmData() {
        disposables.add(crmRepository
            .getCrmData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(crmData -> {
                CrmViewState newState = state
                    .getValue()
                    .toBuilder()
                    .schools(crmData.getSchools())
                    .noteHeaders(crmData.getNotes())
                    .teachers(crmData.getTeachers())
                    .build();

                state.setValue(newState);
            }, error -> {

            }));
    }

    public LiveData<CrmViewState> viewState() {
        return state;
    }

    public void clear() {
        disposables.dispose();
    }
    public void refresh() {
        loadCrmData();
    }
}
