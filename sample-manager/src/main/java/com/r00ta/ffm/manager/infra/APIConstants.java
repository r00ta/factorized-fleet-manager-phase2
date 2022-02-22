package com.r00ta.ffm.manager.infra;

/**
 * Some constants used for URL construction, parameter naming and defaults for the API
 */
public class APIConstants {

    /**
     * Base Path for the user-facing API
     */
    public static final String USER_API_BASE_PATH = "/api/v1/dinosaur/";

    /**
     * Base Path for the error API
     */
    public static final String ERROR_API_BASE_PATH = "/api/v1/errors/";

    /**
     * Base Path for Shard facing API.
     */
    public static final String SHARD_API_BASE_PATH = "/api/v1/shard/dinosaur/";

    /**
     * User id attribute claim key.
     */
    public static final String SUBJECT_ATTRIBUTE_CLAIM = "sub";

    private APIConstants() {
    }
}