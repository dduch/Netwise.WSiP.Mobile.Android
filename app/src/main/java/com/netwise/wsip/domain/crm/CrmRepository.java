package com.netwise.wsip.domain.crm;

import io.reactivex.Single;

public interface CrmRepository {
    Single<CrmData> getCrmData();
}
