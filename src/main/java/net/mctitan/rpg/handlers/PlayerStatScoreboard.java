package net.mctitan.rpg.handlers;

import net.mctitan.rpg.event.PlayerStatScoreboardUpdateEvent;
import net.mctitan.rpg.scoreboard.Scoreboards;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerStatScoreboard implements Listener {
    private static final PlayerStatScoreboard instance = new PlayerStatScoreboard();

    private PlayerStatScoreboard() {}

    public static PlayerStatScoreboard instance() { return instance; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Scoreboards.instance().create(event.getPlayer());
    }

    @EventHandler
    public void onPlayerScoreboard(PlayerStatScoreboardUpdateEvent event) {
        new Thread(() -> {
            Scoreboards.instance().update(event.getPlayer(), event.getStats());
        }).start();
    }
}
