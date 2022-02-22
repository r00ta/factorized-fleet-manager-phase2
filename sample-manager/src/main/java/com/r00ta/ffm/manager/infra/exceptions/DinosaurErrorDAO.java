package com.r00ta.ffm.manager.infra.exceptions;

import com.r00ta.ffm.core.models.ListResult;
import com.r00ta.ffm.core.models.QueryInfo;

public interface DinosaurErrorDAO {

    ListResult<DinosaurError> findAllErrorsByType(QueryInfo queryInfo, DinosaurErrorType type);

    DinosaurError findErrorByIdAndType(int errorId, DinosaurErrorType type);

    DinosaurError findByException(Exception ex);

    DinosaurError findByException(Class clazz);
}
