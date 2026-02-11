package net.mctitan.rpg.scoreboard;

import net.mctitan.rpg.util.stats.Stats;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Scoreboards {
    private static final Scoreboards instance = new Scoreboards();
    private Map<UUID, Scoreboard> scoreboards = new HashMap<>();

    private Scoreboards() {}

    public static Scoreboards instance() { return instance; }

    public void create(Player player) {
        Scoreboard scoreboard = new Scoreboard(player);
        synchronized (scoreboards) { scoreboards.put(player.getUniqueId(), scoreboard); }
        scoreboard.create();
    }

    public void update(Player player, Stats stats) {
        // get the scoreboard
        Scoreboard scoreboard = null;
        do {
            synchronized (scoreboards) {
                scoreboard = scoreboards.get(player.getUniqueId());
            }
        } while(scoreboard == null);

        // send update to scoreboard
        scoreboard.next(stats);
    }
}
