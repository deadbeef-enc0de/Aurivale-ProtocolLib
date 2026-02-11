package net.mctitan.rpg.packethandlers;

import net.mctitan.rpg.AurivaleProtocolLib;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class HideElderGaurdianEffect extends PacketAdapter {
    private static final int GAME_STATE_ELDER_GUARDIAN_REASON = 10;

    public HideElderGaurdianEffect(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.GAME_STATE_CHANGE);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();
        int reason = container.getGameStateIDs().read(0);
        if(reason == GAME_STATE_ELDER_GUARDIAN_REASON) {
            event.setCancelled(true);
        }
    }
}
