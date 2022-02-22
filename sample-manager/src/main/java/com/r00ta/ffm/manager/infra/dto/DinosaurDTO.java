package com.r00ta.ffm.manager.infra.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.r00ta.ffm.core.models.ManagedEntityStatus;

public class DinosaurDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("status")
    private ManagedEntityStatus status;

    public DinosaurDTO() {
    }

    public DinosaurDTO(String id, String name, String customerId, ManagedEntityStatus status) {
        this.id = id;
        this.name = name;
        this.customerId = customerId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ManagedEntityStatus getStatus() {
        return status;
    }

    public void setStatus(ManagedEntityStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DinosaurDTO dinosaurDTO = (DinosaurDTO) o;
        return id.equals(dinosaurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DinosaurDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", customerId='" + customerId + '\'' +
                ", status=" + status + '\'' +
                '}';
    }
}
