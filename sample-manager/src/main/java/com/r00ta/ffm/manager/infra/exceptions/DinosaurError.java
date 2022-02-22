package com.r00ta.ffm.manager.infra.exceptions;

public class DinosaurError {

    private final int id;
    private final String code;
    private final String reason;
    private final DinosaurErrorType type;

    public DinosaurError(int id, String code, String reason, DinosaurErrorType type) {
        super();
        this.id = id;
        this.code = code;
        this.reason = reason;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }

    public DinosaurErrorType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Error [id=" + id + ", code=" + code + ", reason=" + reason + ", type=" + type + "]";
    }
}
