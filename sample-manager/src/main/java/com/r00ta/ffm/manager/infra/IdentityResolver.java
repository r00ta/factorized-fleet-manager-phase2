package com.r00ta.ffm.manager.infra;

import org.eclipse.microprofile.jwt.JsonWebToken;

public interface IdentityResolver {

    String resolve(JsonWebToken jwt);
}
