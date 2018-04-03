package com.netwise.wsip.presentation.schoolTeachers;

import android.arch.lifecycle.ViewModel;

import com.netwise.wsip.domain.crm.CrmRepository;

import javax.inject.Inject;

/**
 * Created by dawido on 19.03.2018.
 */

public class SchoolTeachersViewModel extends ViewModel {

    private final CrmRepository crmRepository;

    @Inject
    public SchoolTeachersViewModel(CrmRepository repository) {
        this.crmRepository = repository;
    }
}