package com.r00ta.ffm.manager.api.models.requests;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.r00ta.ffm.manager.models.Dinosaur;

public class DinosaurRequest {

    @NotEmpty(message = "Dinosaur name cannot be null or empty")
    @JsonProperty("name")
    private String name;

    public DinosaurRequest() {
    }

    public DinosaurRequest(String name) {
        this.name = name;
    }

    public Dinosaur toEntity() {
        return new Dinosaur(name);
    }

    public String getName() {
        return name;
    }
}
