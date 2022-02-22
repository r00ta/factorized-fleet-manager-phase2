package com.r00ta.ffm.manager;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r00ta.ffm.core.AbstractStatusOrchestrator;
import com.r00ta.ffm.core.models.ListResult;
import com.r00ta.ffm.core.models.ManagedEntityStatus;
import com.r00ta.ffm.core.models.QueryInfo;
import com.r00ta.ffm.manager.api.models.requests.DinosaurRequest;
import com.r00ta.ffm.manager.api.models.responses.DinosaurResponse;
import com.r00ta.ffm.manager.dao.DinosaurDAO;
import com.r00ta.ffm.manager.infra.APIConstants;
import com.r00ta.ffm.manager.infra.dto.DinosaurDTO;
import com.r00ta.ffm.manager.infra.exceptions.definitions.user.AlreadyExistingItemException;
import com.r00ta.ffm.manager.infra.exceptions.definitions.user.ItemNotFoundException;
import com.r00ta.ffm.manager.models.Dinosaur;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

@ApplicationScoped
public class DinosaurServiceImpl extends AbstractStatusOrchestrator<Dinosaur> implements DinosaurService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DinosaurServiceImpl.class);

    @Inject
    MeterRegistry meterRegistry;

    @Inject
    ShardService shardService;

    public DinosaurServiceImpl() {
    }

    @Inject
    public DinosaurServiceImpl(DinosaurDAO dinosaurDAO) {
        super(dinosaurDAO);
    }

    @Override
    public Dinosaur createDinosaur(String customerId, DinosaurRequest dinosaurRequest) {
        if (managedEntityDAO.findByNameAndCustomerId(dinosaurRequest.getName(), customerId) != null) {
            throw new AlreadyExistingItemException(String.format("Dinosaur with name '%s' already exists for customer with id '%s'", dinosaurRequest.getName(), customerId));
        }

        Dinosaur dinosaur = dinosaurRequest.toEntity();
        dinosaur.setSubmittedAt(ZonedDateTime.now(ZoneOffset.UTC));
        dinosaur.setCustomerId(customerId);
        dinosaur.setShardId(shardService.getAssignedShardId(dinosaur.getId()));

        // Persist and fire events.
        this.accept(dinosaur);

        LOGGER.info("Dinosaur with id '{}' has been created for customer '{}'", dinosaur.getId(), dinosaur.getCustomerId());
        return dinosaur;
    }

    @Override
    public Dinosaur getDinosaur(String id) {
        Dinosaur d = managedEntityDAO.findById(id);
        if (d == null) {
            throw new ItemNotFoundException(String.format("Dinosaur with id '%s' does not exist", id));
        }
        return d;
    }

    @Override
    public Dinosaur getDinosaur(String id, String customerId) {
        return findByIdAndCustomerId(id, customerId);
    }

    @Override
    public Dinosaur getAvailableDinosaur(String dinosaurId, String customerId) {
        return null;
    }

    @Override
    public void deleteDinosaur(String id, String customerId) {
        Dinosaur dinosaur = findByIdAndCustomerId(id, customerId);
        dinosaur.setStatus(ManagedEntityStatus.DEPROVISION);
        LOGGER.info("Dinosaur with id '{}' for customer '{}' has been marked for deletion", dinosaur.getId(), dinosaur.getCustomerId());
    }

    @Override
    public ListResult<Dinosaur> getDinosaurs(String customerId, QueryInfo queryInfo) {
        return managedEntityDAO.findByCustomerId(customerId, queryInfo);
    }

    @Override
    public List<Dinosaur> getDinosaursByStatusesAndShardId(List<ManagedEntityStatus> statuses, String shardId) {
        return managedEntityDAO.findByStatusesAndShardId(statuses, shardId);
    }

    @Override
    public Dinosaur updateDinosaur(DinosaurDTO dinosaurDTO) {
        Dinosaur dinosaur = getDinosaur(dinosaurDTO.getId(), dinosaurDTO.getCustomerId());
        dinosaur.setStatus(dinosaurDTO.getStatus());

        if (dinosaurDTO.getStatus().equals(ManagedEntityStatus.DELETED)) {
            managedEntityDAO.deleteById(dinosaur.getId());
        }

        // Update metrics
        meterRegistry.counter("manager.dinosaur.status.change",
                Collections.singletonList(Tag.of("status", dinosaurDTO.getStatus().toString()))).increment();

        LOGGER.info("Dinosaur with id '{}' has been updated for customer '{}'", dinosaur.getId(), dinosaur.getCustomerId());
        return dinosaur;
    }

    @Override
    public DinosaurDTO toDTO(Dinosaur dinosaur) {
        DinosaurDTO dto = new DinosaurDTO();
        dto.setId(dinosaur.getId());
        dto.setName(dinosaur.getName());
        dto.setStatus(dinosaur.getStatus());
        dto.setCustomerId(dinosaur.getCustomerId());
        return dto;
    }

    @Override
    public DinosaurResponse toResponse(Dinosaur dinosaur) {
        DinosaurResponse response = new DinosaurResponse();
        response.setId(dinosaur.getId());
        response.setName(dinosaur.getName());
        response.setEndpoint(dinosaur.getEndpoint());
        response.setSubmittedAt(dinosaur.getSubmittedAt());
        response.setPublishedAt(dinosaur.getModifiedAt());
        response.setStatus(dinosaur.getStatus());
        response.setHref(APIConstants.USER_API_BASE_PATH + dinosaur.getId());
        return response;
    }

    private Dinosaur findByIdAndCustomerId(String id, String customerId) {
        Dinosaur dinosaur = managedEntityDAO.findByIdAndCustomerId(id, customerId);
        if (dinosaur == null) {
            throw new ItemNotFoundException(String.format("Dinosaur with id '%s' for customer '%s' does not exist", id, customerId));
        }
        return dinosaur;
    }
}
