package com.r00ta.ffm.core;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r00ta.ffm.core.dao.ManagedEntityDAO;
import com.r00ta.ffm.core.models.ManagedEntity;
import com.r00ta.ffm.core.models.ManagedEntityStatus;

import io.vertx.mutiny.core.eventbus.EventBus;

public abstract class AbstractPreparingWorker<T extends ManagedEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPreparingWorker.class);

    private String busName;

    @Inject
    EventBus eventBus;

    ManagedEntityDAO<T> managedEntityDAO;

    public AbstractPreparingWorker() {
    }

    public AbstractPreparingWorker(ManagedEntityDAO<T> managedEntityDAO, String busName) {
        this.managedEntityDAO = managedEntityDAO;
        this.busName = busName;
    }

    public void fireAccepted(T entity) {
        LOGGER.info("Firing accepted event for entity " + entity.getId());
        eventBus.requestAndForget(busName, entity);
    }

    // To be annotated by the user, since @ConsumeEvents creates the bean automatically!
    public void consumeAccepted(T entity) {
        LOGGER.info("Consuming event for entity " + entity.getId());

        // we should fire here the PREPARING events to the workers defined by the client of the module.
        // The idea is that the user can deploy dependencies before moving the object to the desired PROVISIONING status
        entity.setStatus(ManagedEntityStatus.PREPARING);
        entity.setDesiredStatus(ManagedEntityStatus.PROVISIONING);
        managedEntityDAO.getEntityManager().merge(entity);
    }
}
