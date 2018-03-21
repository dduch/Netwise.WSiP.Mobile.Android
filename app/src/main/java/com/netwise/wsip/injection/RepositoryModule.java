package com.netwise.wsip.injection;

import com.netwise.wsip.domain.crm.CrmRepository;
import com.netwise.wsip.infastucture.RemoteCrmDataRepository;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    abstract CrmRepository bindsCrmRepository(RemoteCrmDataRepository remoteCrmDataRepository);

}
