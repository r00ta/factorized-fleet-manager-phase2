package com.r00ta.ffm.manager.api.models.responses;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.r00ta.ffm.manager.infra.APIConstants;
import com.r00ta.ffm.manager.infra.exceptions.DinosaurError;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends BaseResponse {

    @JsonProperty("code")
    private String code;

    @JsonProperty("reason")
    private String reason;

    protected ErrorResponse() {
        super("Error");
    }

    public static ErrorResponse from(DinosaurError dinosaurError) {
        ErrorResponse response = new ErrorResponse();
        response.setId(Integer.toString(dinosaurError.getId()));
        response.setCode(dinosaurError.getCode());
        response.setReason(dinosaurError.getReason());
        response.setHref(APIConstants.ERROR_API_BASE_PATH + dinosaurError.getId());
        return response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, reason);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ErrorResponse other = (ErrorResponse) obj;
        return Objects.equals(code, other.code) && Objects.equals(reason, other.reason);
    }
}
