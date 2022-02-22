package com.r00ta.ffm.manager.infra.exceptions.mappers;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r00ta.ffm.manager.api.models.responses.ErrorResponse;
import com.r00ta.ffm.manager.infra.exceptions.DinosaurError;
import com.r00ta.ffm.manager.infra.exceptions.DinosaurErrorService;
import com.r00ta.ffm.manager.infra.exceptions.definitions.user.ExternalUserException;

public class ExternalUserExceptionMapper implements ExceptionMapper<ExternalUserException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalUserExceptionMapper.class);

    @Inject
    DinosaurErrorService dinosaurErrorService;

    @Override
    public Response toResponse(ExternalUserException e) {
        LOGGER.debug("Failure", e);
        Optional<DinosaurError> error = dinosaurErrorService.getError(e);
        ResponseBuilder builder = Response.status(e.getStatusCode());
        if (error.isPresent()) {
            ErrorResponse errorResponse = ErrorResponse.from(error.get());
            errorResponse.setReason(e.getMessage());
            builder.entity(errorResponse);
        } else {
            LOGGER.warn("Information for exception type {} cannot be found", e.getClass());
            builder.entity(e.getMessage());
        }
        return builder.build();
    }
}
