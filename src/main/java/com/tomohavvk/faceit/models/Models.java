package com.tomohavvk.faceit.models;

import com.tomohavvk.faceit.serialization.SnakeCase;

import java.util.List;

public class Models {

    public record PlayerStats(
            String playerId,
            String nickname,
            Integer totalKills,
            Integer totalDeath,
            Integer totalAssists,
            Integer totalHeadshots,
            Double averageKills,
            Double averageDeath,
            Double averageAssists,
            Double averageHeadshots,
            Double averageKd,
            Double averageKr,
            Integer matchesAnalyzed
    ) implements SnakeCase {
    }

    public record TeamsStats(List<PlayerStats> teamLeft, List<PlayerStats> teamRight) implements SnakeCase {
    }
}
