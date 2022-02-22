package com.r00ta.ffm.manager.infra.exceptions;

import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.r00ta.ffm.core.models.ListResult;
import com.r00ta.ffm.core.models.QueryInfo;

@ApplicationScoped
public class DinosaurErrorServiceImpl implements DinosaurErrorService {

    @Inject
    DinosaurErrorDAO repository;

    @Override
    public ListResult<DinosaurError> getUserErrors(QueryInfo queryInfo) {
        return repository.findAllErrorsByType(queryInfo, DinosaurErrorType.USER);
    }

    @Override
    public Optional<DinosaurError> getUserError(int errorId) {
        return Optional.ofNullable(repository.findErrorByIdAndType(errorId, DinosaurErrorType.USER));
    }

    @Override
    public Optional<DinosaurError> getError(Exception e) {
        return Optional.ofNullable(repository.findByException(e));
    }

    @Override
    public Optional<DinosaurError> getError(Class clazz) {
        return Optional.ofNullable(repository.findByException(clazz));
    }
}
