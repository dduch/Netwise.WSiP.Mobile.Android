package com.netwise.wsip.infastucture;

import com.netwise.wsip.domain.crm.CrmData;
import com.netwise.wsip.domain.crm.CrmRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class RemoteCrmDataRepository implements CrmRepository {
    CrmAllDataDataSource dataDataSource;
    CrmData cacheCrmData;

    @Inject
    public RemoteCrmDataRepository(CrmAllDataDataSource dataDataSource) {
        this.dataDataSource = dataDataSource;
    }

    @Override
    public Single<CrmData> getCrmData() {

        if (cacheCrmData != null) {
            return Single.just(cacheCrmData);
        }

        return dataDataSource
            .getAllData()
            .subscribeOn(Schedulers.io())
            .doOnSuccess(crmData -> cacheCrmData = crmData);
    }
}
