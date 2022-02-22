package com.r00ta.ffm.manager.api.internal;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r00ta.ffm.core.models.ManagedEntityStatus;
import com.r00ta.ffm.manager.DinosaurService;
import com.r00ta.ffm.manager.ShardService;
import com.r00ta.ffm.manager.infra.APIConstants;
import com.r00ta.ffm.manager.infra.IdentityResolver;
import com.r00ta.ffm.manager.infra.dto.DinosaurDTO;
import com.r00ta.ffm.manager.infra.exceptions.definitions.user.ForbiddenRequestException;

import io.quarkus.security.Authenticated;

import static java.util.stream.Collectors.toList;

// To be moved to core since it can be generalized
@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "bearer",
                type = SecuritySchemeType.HTTP,
                scheme = "Bearer")
})
@SecurityRequirement(name = "bearer")
@Path(APIConstants.SHARD_API_BASE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authenticated
public class ShardSyncAPI {

    private static final List<ManagedEntityStatus> statuses = Arrays.asList(ManagedEntityStatus.PREPARING, ManagedEntityStatus.DEPROVISION);
    private static final Logger LOGGER = LoggerFactory.getLogger(ShardSyncAPI.class);

    @Inject
    DinosaurService dinosaurService;

    @Inject
    ShardService shardService;

    @Inject
    IdentityResolver identityResolver;

    @Inject
    JsonWebToken jwt;

    @GET
    public Response getDinosaurs() {
        String shardId = identityResolver.resolve(jwt);
        failIfNotAuthorized(shardId);
        LOGGER.info("Shard asks for Dinosaurs to deploy or delete");
        return Response.ok(dinosaurService.getDinosaursByStatusesAndShardId(statuses, shardId)
                .stream()
                .map(dinosaurService::toDTO)
                .collect(toList()))
                .build();
    }

    @PUT
    public Response updateDinosaur(DinosaurDTO dto) {
        String subject = identityResolver.resolve(jwt);
        failIfNotAuthorized(subject);
        LOGGER.info("Shard wants to update the Dinosaur with id '{}' with the status '{}'", dto.getId(), dto.getStatus());
        dinosaurService.updateDinosaur(dto);
        return Response.ok().build();
    }

    private void failIfNotAuthorized(String shardId) {
        if (!shardService.isAuthorizedShard(shardId)) {
            throw new ForbiddenRequestException(String.format("User '%s' is not authorized to access this api.", shardId));
        }
    }
}
