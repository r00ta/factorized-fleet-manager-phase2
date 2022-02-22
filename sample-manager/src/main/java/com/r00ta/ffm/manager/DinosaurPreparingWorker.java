package com.r00ta.ffm.manager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import com.r00ta.ffm.core.AbstractPreparingWorker;
import com.r00ta.ffm.manager.dao.DinosaurDAO;
import com.r00ta.ffm.manager.models.Dinosaur;

import io.quarkus.vertx.ConsumeEvent;

@ApplicationScoped
public class DinosaurPreparingWorker extends AbstractPreparingWorker<Dinosaur> {

    private static final String DINOSAUR_PREPARING_BUS_NAME = "dinosaur-preparing-bus";

    public DinosaurPreparingWorker() {
    }

    @Inject
    public DinosaurPreparingWorker(DinosaurDAO dinosaurDAO) {
        super(dinosaurDAO, DINOSAUR_PREPARING_BUS_NAME);
    }

    @Override
    @Transactional
    @ConsumeEvent(value = DINOSAUR_PREPARING_BUS_NAME, blocking = true)
    public void consumeAccepted(Dinosaur dinosaur) {
        super.consumeAccepted(dinosaur);
    }
}
