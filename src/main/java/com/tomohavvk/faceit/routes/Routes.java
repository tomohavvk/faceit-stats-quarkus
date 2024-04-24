package com.tomohavvk.faceit.routes;

import com.tomohavvk.faceit.models.Models.TeamsStats;
import com.tomohavvk.faceit.services.Service;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/api/v1")
public class Routes {

    private final Service service;

    public Routes(Service service) {
        this.service = service;
    }

    @GET
    @Path("/match/{matchId}")
    public Uni<TeamsStats> getMatchPlayersStats(@PathParam("matchId") String matchId) {
        return service.getMatchPlayersStats(matchId);
    }
}
