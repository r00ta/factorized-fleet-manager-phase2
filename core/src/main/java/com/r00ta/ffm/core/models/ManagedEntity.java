package com.r00ta.ffm.core.models;

import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class ManagedEntity {
    @Id
    private String id = UUID.randomUUID().toString();

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "customer_id", nullable = false, updatable = false)
    private String customerId;

    @Column(name = "submitted_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
    private ZonedDateTime submittedAt;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private ZonedDateTime modifiedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ManagedEntityStatus status;

    @Column(name = "desiredStatus")
    @Enumerated(EnumType.STRING)
    private ManagedEntityStatus desiredStatus;

    @Column(name = "shard_id")
    private String shardId;

    public ManagedEntity() {
    }

    public ManagedEntity(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ZonedDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(ZonedDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public ManagedEntityStatus getStatus() {
        return status;
    }

    public void setStatus(ManagedEntityStatus status) {
        this.status = status;
    }

    public String getShardId() {
        return shardId;
    }

    public void setShardId(String shardId) {
        this.shardId = shardId;
    }

    public ManagedEntityStatus getDesiredStatus() {
        return desiredStatus;
    }

    public void setDesiredStatus(ManagedEntityStatus desiredStatus) {
        this.desiredStatus = desiredStatus;
    }
}
