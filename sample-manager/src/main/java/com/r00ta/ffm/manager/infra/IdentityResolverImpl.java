package com.r00ta.ffm.manager.infra;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class IdentityResolverImpl implements IdentityResolver {

    @Override
    public String resolve(JsonWebToken jwt) {
        return jwt.getClaim(APIConstants.SUBJECT_ATTRIBUTE_CLAIM);
    }
}
