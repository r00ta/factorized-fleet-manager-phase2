package com.r00ta.ffm.manager.dao;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.r00ta.ffm.core.dao.ManagedEntityDAO;
import com.r00ta.ffm.core.models.ListResult;
import com.r00ta.ffm.core.models.ManagedEntityStatus;
import com.r00ta.ffm.core.models.QueryInfo;
import com.r00ta.ffm.manager.models.Dinosaur;

import io.quarkus.panache.common.Parameters;

@ApplicationScoped
@Transactional
public class DinosaurDAO implements ManagedEntityDAO<Dinosaur> {

    public List<Dinosaur> findByStatusesAndShardId(List<ManagedEntityStatus> statuses, String shardId) {
        Parameters params = Parameters
                .with("statuses", statuses)
                .and("shardId", shardId);
        return find("#DINOSAUR.findByStatusesAndShardId", params).list();
    }

    public Dinosaur findByNameAndCustomerId(String name, String customerId) {
        Parameters params = Parameters
                .with("name", name).and("customerId", customerId);
        return find("#DINOSAUR.findByNameAndCustomerId", params).firstResult();
    }

    public Dinosaur findByIdAndCustomerId(String id, String customerId) {
        Parameters params = Parameters
                .with("id", id).and("customerId", customerId);
        return find("#DINOSAUR.findByIdAndCustomerId", params).firstResult();
    }

    public ListResult<Dinosaur> findByCustomerId(String customerId, QueryInfo queryInfo) {
        Parameters parameters = Parameters.with("customerId", customerId);
        long total = find("#DINOSAUR.findByCustomerId", parameters).count();
        List<Dinosaur> dinosaurs = find("#DINOSAUR.findByCustomerId", parameters).page(queryInfo.getPageNumber(), queryInfo.getPageSize()).list();
        return new ListResult<>(dinosaurs, queryInfo.getPageNumber(), total);
    }
}
