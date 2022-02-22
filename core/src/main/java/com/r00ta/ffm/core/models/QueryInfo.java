package com.r00ta.ffm.core.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import com.r00ta.ffm.core.APIConstants;

// TODO add sorting info to this class in future
public class QueryInfo {

    @DefaultValue(APIConstants.PAGE_DEFAULT)
    @Min(APIConstants.PAGE_MIN)
    @QueryParam(APIConstants.PAGE)
    private int pageNumber;
    @DefaultValue(APIConstants.SIZE_DEFAULT)
    @Min(APIConstants.SIZE_MIN)
    @Max(APIConstants.SIZE_MAX)
    @QueryParam(APIConstants.PAGE_SIZE)
    private int pageSize;

    public QueryInfo() {
    }

    public QueryInfo(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String toString() {
        return "QueryInfo [pageNumber=" + pageNumber + ", pageSize=" + pageSize + "]";
    }
}
