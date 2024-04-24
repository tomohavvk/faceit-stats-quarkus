package com.tomohavvk.faceit.services;

import com.tomohavvk.faceit.client.FaceitApi;
import com.tomohavvk.faceit.client.FaceitModels.Player;
import com.tomohavvk.faceit.client.FaceitModels.Stats;
import com.tomohavvk.faceit.models.Models;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@ApplicationScoped
public class ServiceImpl implements Service {

    private final FaceitApi faceitApi;

    public ServiceImpl(@RestClient FaceitApi faceitApi) {
        this.faceitApi = faceitApi;
    }

    private record PlayerWithStats(Player player, Stats stats) {

    }

    @Override
    public Uni<Models.TeamsStats> getMatchPlayersStats(@PathParam("matchId") String matchId) {
        return faceitApi.getMatch(matchId)
                .flatMap(matchStats -> {
                    var teamFirst = matchStats.teams().faction1().players();
                    var teamSecond = matchStats.teams().faction2().players();
                    return Multi.createFrom().completionStage(getPlayersStats(teamFirst).subscribe().asCompletionStage()).toUni()
                            .flatMap(teamFirstStats -> getPlayersStats(teamSecond).map(teamSecondStats -> {
                                var teamFirstPlayersInfo = getTeamPlayersInfo(teamFirstStats);
                                var teamSecondPlayersInfo = getTeamPlayersInfo(teamSecondStats);

                                return new Models.TeamsStats(teamFirstPlayersInfo, teamSecondPlayersInfo);
                            }));
                });
    }

    private static List<Models.PlayerStats> getTeamPlayersInfo(List<PlayerWithStats> teamStats) {
        return teamStats.stream()
                .map(playerWithStats -> {
                    var matches = new BigDecimal(playerWithStats.stats.items().size());

                    var playerId = playerWithStats.player.playerId();
                    var nickname = playerWithStats.player.nickname();
                    var totalKills = getTotalByKey("Kills", playerWithStats);
                    var totalDeath = getTotalByKey("Deaths", playerWithStats);
                    var totalAssists = getTotalByKey("Assists", playerWithStats);
                    var totalHeadshots = getTotalByKey("Headshots", playerWithStats);
                    var totalKd = getTotalByKey("K/D Ratio", playerWithStats);
                    var totalKr = getTotalByKey("K/R Ratio", playerWithStats);
                    var averageKills = totalKills.divide(matches, new MathContext(2, RoundingMode.HALF_EVEN));
                    var averageDeath = totalDeath.divide(matches, new MathContext(2, RoundingMode.HALF_EVEN));
                    var averageAssists = totalAssists.divide(matches, new MathContext(2, RoundingMode.HALF_EVEN));
                    var averageHeadshots = totalHeadshots.divide(matches, new MathContext(2, RoundingMode.HALF_EVEN));
                    var averageKd = totalKd.divide(matches, new MathContext(2, RoundingMode.HALF_EVEN));
                    var averageKr = totalKr.divide(matches, new MathContext(2, RoundingMode.HALF_EVEN));

                    return new Models.PlayerStats(playerId, nickname, totalKills.intValue(), totalDeath.intValue(), totalAssists.intValue(), totalHeadshots.intValue(), averageKills.doubleValue(), averageDeath.doubleValue(), averageAssists.doubleValue(), averageHeadshots.doubleValue(), averageKd.doubleValue(), averageKr.doubleValue(), matches.intValue());
                }).toList();
    }

    private static BigDecimal getTotalByKey(String key, PlayerWithStats playerWithStats) {
        return playerWithStats.stats.items().stream().map(x -> new BigDecimal(x.stats().getOrDefault(key, "0.0"))).reduce(new BigDecimal(0), BigDecimal::add);
    }

    private Uni<List<PlayerWithStats>> getPlayersStats(List<Player> players) {
        return Multi.createFrom().items(players.stream())
                .flatMap(player -> faceitApi.getPlayerStats(player.playerId(), 0, 100)
                        .map(playerStats -> new PlayerWithStats(player, playerStats)).toMulti()).collect().asList();
    }

}
