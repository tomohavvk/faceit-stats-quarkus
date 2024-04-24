package com.tomohavvk.faceit.exceptions;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Provider
public class ApplicationExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        var traceId = UUID.randomUUID().toString();
        log.error("|{}| {}", traceId, exception.getMessage(), exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("traceId", traceId, "error", "Internal Server Error")).build();
    }
}