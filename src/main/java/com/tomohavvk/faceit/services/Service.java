package com.tomohavvk.faceit.services;

import com.tomohavvk.faceit.models.Models.TeamsStats;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.PathParam;


public interface Service {
    Uni<TeamsStats> getMatchPlayersStats(@PathParam("matchId") String matchId);
}
