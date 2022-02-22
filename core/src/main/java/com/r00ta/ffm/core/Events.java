package com.r00ta.ffm.core;

public class Events {

    private static final String ACCEPTED_EVENT_PREFIX = "-entity-accepted";
    private static final String DEPROVISION_EVENT_PREFIX = "-entity-deprovision";

    public static String buildAcceptedEvent(String entityName) {
        return entityName + ACCEPTED_EVENT_PREFIX;
    }

    public static String buildDeprovisionEvent(String entityName) {
        return entityName + DEPROVISION_EVENT_PREFIX;
    }
}
