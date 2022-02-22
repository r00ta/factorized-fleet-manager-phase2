package com.r00ta.ffm.manager;

import java.util.List;

import com.r00ta.ffm.core.StatusOrchestrator;
import com.r00ta.ffm.core.models.ListResult;
import com.r00ta.ffm.core.models.ManagedEntityStatus;
import com.r00ta.ffm.core.models.QueryInfo;
import com.r00ta.ffm.manager.api.models.requests.DinosaurRequest;
import com.r00ta.ffm.manager.api.models.responses.DinosaurResponse;
import com.r00ta.ffm.manager.infra.dto.DinosaurDTO;
import com.r00ta.ffm.manager.models.Dinosaur;

public interface DinosaurService extends StatusOrchestrator<Dinosaur> {

    Dinosaur createDinosaur(String customerId, DinosaurRequest dinosaurRequest);

    Dinosaur getDinosaur(String id);

    Dinosaur getDinosaur(String id, String customerId);

    Dinosaur getAvailableDinosaur(String dinosaurId, String customerId);

    void deleteDinosaur(String id, String customerId);

    ListResult<Dinosaur> getDinosaurs(String customerId, QueryInfo queryInfo);

    List<Dinosaur> getDinosaursByStatusesAndShardId(List<ManagedEntityStatus> statuses, String shardId);

    Dinosaur updateDinosaur(DinosaurDTO dinosaurDTO);

    DinosaurDTO toDTO(Dinosaur dinosaur);

    DinosaurResponse toResponse(Dinosaur dinosaur);
}
