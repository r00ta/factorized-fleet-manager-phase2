package com.r00ta.ffm.manager.infra.exceptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.r00ta.ffm.core.models.ListResult;
import com.r00ta.ffm.core.models.QueryInfo;
import com.r00ta.ffm.manager.infra.Constants;
import com.r00ta.ffm.manager.infra.exceptions.definitions.user.ItemNotFoundException;

import io.quarkus.runtime.Quarkus;

@ApplicationScoped
public class DinosaurErrorInMemoryDAO implements DinosaurErrorDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DinosaurErrorInMemoryDAO.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Map<Integer, DinosaurError> dinosaurErrorsFromId = new HashMap<>();
    private final Map<String, DinosaurError> errorsFromExc = new HashMap<>();
    private final List<DinosaurError> dinosaurErrorList = new ArrayList<>();

    @PostConstruct
    void init() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("exception/exceptionInfo.json")) {
            if (is != null) {
                MAPPER.readValue(is, new TypeReference<List<ErrorInfo>>() {
                }).forEach(this::populate);
            } else {
                LOGGER.error("Cannot find file containing errors in classpath");
                Quarkus.asyncExit(1);
            }
        } catch (IOException io) {
            LOGGER.error("Error closing file with exception errors", io);
            Quarkus.asyncExit(1);
        }
    }

    private void populate(ErrorInfo errorInfo) {
        DinosaurError dinosaurError = errorInfo.toError();
        dinosaurErrorList.add(dinosaurError);
        dinosaurErrorsFromId.put(dinosaurError.getId(), dinosaurError);
        errorsFromExc.put(errorInfo.getException(), dinosaurError);
    }

    @Override
    public ListResult<DinosaurError> findAllErrorsByType(QueryInfo queryInfo, DinosaurErrorType type) {
        int start = queryInfo.getPageNumber() * queryInfo.getPageSize();
        List<DinosaurError> typedErrors = dinosaurErrorList.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
        return new ListResult<>(
                start >= typedErrors.size() ? Collections.emptyList() : typedErrors.subList(start, Math.min(start + queryInfo.getPageSize(), typedErrors.size())),
                queryInfo.getPageNumber(),
                typedErrors.size());
    }

    @Override
    public DinosaurError findErrorByIdAndType(int errorId, DinosaurErrorType type) {
        DinosaurError error = dinosaurErrorsFromId.get(errorId);
        if (!error.getType().equals(type)) {
            throw new ItemNotFoundException(String.format("Error with id %s and type %s not found in the catalog", errorId, type));
        }
        return error;
    }

    @Override
    public DinosaurError findByException(Exception ex) {
        return errorsFromExc.get(ex.getClass().getName());
    }

    @Override
    public DinosaurError findByException(Class clazz) {
        return errorsFromExc.get(clazz.getName());
    }

    public static class ErrorInfo {

        @JsonProperty("exception")
        private String exception;

        @JsonProperty("id")
        private int id;

        @JsonProperty("reason")
        private String reason;

        @JsonProperty("type")
        private DinosaurErrorType type;

        public String getException() {
            return exception;
        }

        public int getId() {
            return id;
        }

        public String getReason() {
            return reason;
        }

        public DinosaurErrorType getType() {
            return type;
        }

        public DinosaurError toError() {
            return new DinosaurError(id, Constants.API_IDENTIFIER_PREFIX + id, reason, type);
        }
    }
}
