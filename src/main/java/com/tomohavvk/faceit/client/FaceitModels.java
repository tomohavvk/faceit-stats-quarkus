package com.tomohavvk.faceit.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tomohavvk.faceit.serialization.SnakeCase;

import java.util.List;
import java.util.Map;

public class FaceitModels {
    public record Player(String playerId, String nickname, String gameSkillLevel) implements SnakeCase {
    }

    public record Faction(String name, @JsonProperty("roster") List<Player> players) implements SnakeCase {
    }

    public record Teams(Faction faction1, Faction faction2) implements SnakeCase {
    }

    public record Match(Teams teams) implements SnakeCase {
    }

    public record Items(Map<String, String> stats) implements SnakeCase {
    }

    public record Stats(List<Items> items) implements SnakeCase {
    }

    public record PlayerModel(String playerId, String nickname) implements SnakeCase {
    }

}
