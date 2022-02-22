package com.r00ta.ffm.core;

import com.r00ta.ffm.core.models.ManagedEntity;

public interface StatusOrchestrator<T extends ManagedEntity> {
    void accept(T entity);
}
