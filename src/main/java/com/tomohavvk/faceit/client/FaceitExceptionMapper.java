package com.tomohavvk.faceit.client;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import java.util.List;

@Slf4j
@Provider
public class FaceitExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    @Override
    public RuntimeException toThrowable(Response response) {
        log.error("faceit api response: {}", response.readEntity(String.class));

        if (List.of(400, 500).contains(response.getStatus())) {
            return new RuntimeException(String.format("The Faceit service responded with code: %d", response.getStatus()));
        }

        return null;
    }
}