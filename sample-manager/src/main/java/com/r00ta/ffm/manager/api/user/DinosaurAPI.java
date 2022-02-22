package com.r00ta.ffm.manager.api.user;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.security.SecuritySchemes;

import com.r00ta.ffm.core.models.QueryInfo;
import com.r00ta.ffm.manager.DinosaurService;
import com.r00ta.ffm.manager.api.models.requests.DinosaurRequest;
import com.r00ta.ffm.manager.api.models.responses.DinosaurListResponse;
import com.r00ta.ffm.manager.api.models.responses.ListResponse;
import com.r00ta.ffm.manager.infra.APIConstants;
import com.r00ta.ffm.manager.infra.IdentityResolver;
import com.r00ta.ffm.manager.models.Dinosaur;

import io.quarkus.security.Authenticated;

@SecuritySchemes(value = {
        @SecurityScheme(securitySchemeName = "bearer",
                type = SecuritySchemeType.HTTP,
                scheme = "Bearer")
})
@SecurityRequirement(name = "bearer")
@Path(APIConstants.USER_API_BASE_PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class DinosaurAPI {

    @Inject
    IdentityResolver identityResolver;

    @Inject
    DinosaurService dinosaurService;

    @Inject
    JsonWebToken jwt;

    @GET
    public Response getDinosaurs(@Valid @BeanParam QueryInfo queryInfo) {
        System.out.println(jwt.getSubject());
        return Response.ok(ListResponse.fill(dinosaurService
                .getDinosaurs(identityResolver.resolve(jwt), queryInfo), new DinosaurListResponse(), dinosaurService::toResponse)).build();
    }

    @POST
    public Response createDinosaur(DinosaurRequest dinosaurRequest) {
        Dinosaur dinosaur = dinosaurService.createDinosaur(identityResolver.resolve(jwt), dinosaurRequest);
        return Response.status(Response.Status.CREATED).entity(dinosaurService.toResponse(dinosaur)).build();
    }

    @GET
    @Path("{id}")
    public Response getDinosaur(@PathParam("id") @NotEmpty String id) {
        Dinosaur dinosaur = dinosaurService.getDinosaur(id, identityResolver.resolve(jwt));
        return Response.ok(dinosaurService.toResponse(dinosaur)).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteDinosaur(@PathParam("id") String id) {
        dinosaurService.deleteDinosaur(id, identityResolver.resolve(jwt));
        return Response.accepted().build();
    }
}
