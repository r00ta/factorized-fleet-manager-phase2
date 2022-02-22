package com.r00ta.ffm.manager.infra.exceptions.definitions.platform;

public class OidcTokensNotInitializedException extends InternalPlatformException {

    public OidcTokensNotInitializedException(String message) {
        super(message);
    }

    public OidcTokensNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }
}
