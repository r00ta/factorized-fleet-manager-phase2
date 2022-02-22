package com.r00ta.ffm.manager.api;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ext.Provider;

import com.r00ta.ffm.manager.infra.exceptions.mappers.ExternalUserExceptionMapper;

@Provider
@ApplicationScoped
public class ManagerExternalUserExceptionMapper extends ExternalUserExceptionMapper {

}
