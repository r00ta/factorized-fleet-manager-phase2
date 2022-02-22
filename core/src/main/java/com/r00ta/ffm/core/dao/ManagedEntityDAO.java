package com.r00ta.ffm.core.dao;

import java.util.List;

import com.r00ta.ffm.core.models.ListResult;
import com.r00ta.ffm.core.models.ManagedEntity;
import com.r00ta.ffm.core.models.ManagedEntityStatus;
import com.r00ta.ffm.core.models.QueryInfo;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

public interface ManagedEntityDAO<T extends ManagedEntity> extends PanacheRepositoryBase<T, String> {
    List<T> findByStatusesAndShardId(List<ManagedEntityStatus> statuses, String shardId);

    T findByNameAndCustomerId(String name, String customerId);

    T findByIdAndCustomerId(String id, String customerId);

    ListResult<T> findByCustomerId(String customerId, QueryInfo queryInfo);
}
