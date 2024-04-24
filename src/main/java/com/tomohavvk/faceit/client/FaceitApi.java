package com.tomohavvk.faceit.client;

import com.tomohavvk.faceit.client.FaceitModels.Match;
import com.tomohavvk.faceit.client.FaceitModels.PlayerModel;
import com.tomohavvk.faceit.client.FaceitModels.Stats;
import io.quarkus.rest.client.reactive.ClientQueryParam;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@ApplicationScoped
@RegisterRestClient(configKey = "faceit-api")
@ClientQueryParam(name = "game", value = "cs2")
@ClientHeaderParam(name = "Authorization", value = "Bearer " + "${faceit.api.api-key}") // FIXME
public interface FaceitApi {
    // TODO move v4 to base if possible path

    @GET
    @Path("/v4/players")
    @Retry(maxRetries = 5, delay = 300)
    Uni<PlayerModel> getPlayer(@QueryParam("nickname") String nickname);

    @GET
    @Path("/v4/matches/{matchId}")
    @Retry(maxRetries = 5, delay = 300)
    Uni<Match> getMatch(@PathParam("matchId") String matchId);

    @GET
    @Path("/v4/players/{playerId}/games/cs2/stats")
    @Retry(maxRetries = 5, delay = 300)
    Uni<Stats> getPlayerStats(@PathParam("playerId") String playerId, @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);
}

