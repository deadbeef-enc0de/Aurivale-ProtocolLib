package net.mctitan.rpg.scoreboard;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.mctitan.rpg.AurivaleProtocolLib;
import net.mctitan.rpg.enums.ScoreboardStat;
import net.mctitan.rpg.util.stats.Stats;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

public class Scoreboard {
    private static final String OBJECTIVE_NAME = "player_stats";
    private static final Component PLAYER_STATS = Component.text("Player Stats").color(NamedTextColor.GOLD);
    private Player player;
    private Map<ScoreboardStat, Component> scoreboard = null;

    public Scoreboard(Player player) {
        this.player = player;
    }

    synchronized public void next(Stats nextstats) {
        // go through all stats and see what needs to be created, updated, or removed
        Map<ScoreboardStat, Component> nextboard = nextstats.lines();
        for(ScoreboardStat scoreboardstat : ScoreboardStat.values()) {
            Component prev = (scoreboard != null ? scoreboard.get(scoreboardstat) : null);
            Component next = nextboard.get(scoreboardstat);

            // create entry in scoreboard
            if(next != null) {
                entry(scoreboardstat, next);
            }

            // remove entry
            else if(prev != null) {
                remove(scoreboardstat);
            }
        }

        // replace scoreboard
        scoreboard = nextboard;
    }

    public void create() {
        // send objective create packet
        PacketContainer objective_packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        objective_packet.getStrings().write(0, OBJECTIVE_NAME);
        objective_packet.getIntegers().write(0, 0);
        objective_packet.getChatComponents().write(0, WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(PLAYER_STATS)));
        AurivaleProtocolLib.instance().protocol().sendServerPacket(player, objective_packet);

        // send display objective packet
        PacketContainer display_packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE);
        display_packet.getEnumModifier(EnumWrappers.DisplaySlot.class, 0).write(0, EnumWrappers.DisplaySlot.SIDEBAR);
        display_packet.getStrings().write(0, OBJECTIVE_NAME);
        AurivaleProtocolLib.instance().protocol().sendServerPacket(player, display_packet);
    }

    public void entry(ScoreboardStat scoreboardstat, Component value) {
        // send scoreboard update (mode = 2)
        PacketContainer objective_packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_OBJECTIVE);
        objective_packet.getStrings().write(0, OBJECTIVE_NAME);
        objective_packet.getIntegers().write(0, 2);
        objective_packet.getChatComponents().write(0, WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(PLAYER_STATS)));
        AurivaleProtocolLib.instance().protocol().sendServerPacket(player, objective_packet);

        // create scoreboard entry packet
        PacketContainer entry_packet = new PacketContainer(PacketType.Play.Server.SCOREBOARD_SCORE);
        entry_packet.getStrings().write(0, scoreboardstat.key());
        entry_packet.getStrings().write(1, OBJECTIVE_NAME);
        entry_packet.getIntegers().write(0, ScoreboardStat.values().length - scoreboardstat.ordinal());

        // add custom entry text
        String value_json = GsonComponentSerializer.gson().serialize(value);
        try {
            Class clazz = WrappedChatComponent.class;
            Method method = clazz.getDeclaredMethod("deserialize", String.class);
            method.setAccessible(true);
            Object object = method.invoke(null, value_json);
            InternalStructure structure = new InternalStructure(object, new StructureModifier<>(clazz));
            entry_packet.getOptionalStructures().write(0, Optional.of(structure));
        } catch(Exception e) {
            e.printStackTrace();
        }

        // set number format to blank so it doesn't show up
        try {
            Class clazz = Class.forName("net.minecraft.network.chat.numbers.BlankFormat");
            Field field = clazz.getDeclaredField("INSTANCE");
            field.setAccessible(true);
            Object object = field.get(null);
            InternalStructure structure = new InternalStructure(object, new StructureModifier<>(clazz));
            entry_packet.getOptionalStructures().write(1, Optional.of(structure));
        } catch(Exception e) {
            e.printStackTrace();
        }

        // send scoreboard entry packet
        AurivaleProtocolLib.instance().protocol().sendServerPacket(player, entry_packet);
    }

    public void remove(ScoreboardStat scoreboardstat) {
        // send entry remove packet
        PacketContainer remove_packet = new PacketContainer(PacketType.Play.Server.RESET_SCORE);
        remove_packet.getStrings().write(0, scoreboardstat.key());
        remove_packet.getStrings().write(1, OBJECTIVE_NAME);
        AurivaleProtocolLib.instance().protocol().sendServerPacket(player, remove_packet);
    }
}
