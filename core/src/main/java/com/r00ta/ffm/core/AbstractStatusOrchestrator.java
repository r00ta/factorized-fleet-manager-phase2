package com.r00ta.ffm.core;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r00ta.ffm.core.dao.ManagedEntityDAO;
import com.r00ta.ffm.core.models.ManagedEntity;
import com.r00ta.ffm.core.models.ManagedEntityStatus;

public abstract class AbstractStatusOrchestrator<T extends ManagedEntity> implements StatusOrchestrator<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStatusOrchestrator.class);

    @Inject
    AbstractPreparingWorker<T> abstractPreparingWorker;

    protected ManagedEntityDAO<T> managedEntityDAO;

    public AbstractStatusOrchestrator() {
    }

    public AbstractStatusOrchestrator(ManagedEntityDAO<T> managedEntityDAO) {
        this.managedEntityDAO = managedEntityDAO;
    }

    @Override
    public void accept(T entity) {
        LOGGER.info("Accept entity " + entity.getId());
        if (entity.getStatus() != null) {
            LOGGER.warn("The status of the entity is not null. It will be overwritten.");
        }
        entity.setStatus(ManagedEntityStatus.ACCEPTED);
        entity.setDesiredStatus(ManagedEntityStatus.PREPARING);

        // Persist and fire
        transact(entity);
        abstractPreparingWorker.fireAccepted(entity);
    }

    @Transactional
    private void transact(T entity){
        managedEntityDAO.persist(entity);
    }
}
