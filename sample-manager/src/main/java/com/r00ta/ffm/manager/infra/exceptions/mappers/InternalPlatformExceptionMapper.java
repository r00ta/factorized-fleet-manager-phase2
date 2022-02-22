package com.r00ta.ffm.manager.infra.exceptions.mappers;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r00ta.ffm.manager.api.models.responses.ErrorResponse;
import com.r00ta.ffm.manager.infra.exceptions.DinosaurError;
import com.r00ta.ffm.manager.infra.exceptions.DinosaurErrorService;
import com.r00ta.ffm.manager.infra.exceptions.definitions.platform.InternalPlatformException;

import io.quarkus.runtime.Quarkus;

public class InternalPlatformExceptionMapper implements ExceptionMapper<InternalPlatformException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalPlatformExceptionMapper.class);
    private static final String MESSAGE_TEMPLATE = "There was an internal exception that is not fixable from the user. Please " +
            "open a bug with all the information you have, including the name of the exception %s and the message %s";
    @Inject
    DinosaurErrorService dinosaurErrorService;
    private DinosaurError internalPlatformExceptionDinosaurError;

    @PostConstruct
    void init() {
        Optional<DinosaurError> error = dinosaurErrorService.getError(InternalPlatformException.class);
        if (error.isPresent()) {
            internalPlatformExceptionDinosaurError = error.get();
        } else {
            LOGGER.error("InternalPlatformException error is not defined in the ErrorsService.");
            Quarkus.asyncExit(1);
        }
    }

    @Override
    public Response toResponse(InternalPlatformException e) {
        LOGGER.warn("Internal Platform exception", e);

        // InternalPlatformException is always returned.
        Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = ErrorResponse.from(internalPlatformExceptionDinosaurError);
        errorResponse.setReason(String.format(MESSAGE_TEMPLATE, e.getClass().getName(), e.getMessage()));
        builder.entity(errorResponse);
        return builder.build();
    }
}
