package net.mctitan.rpg.packethandlers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.mctitan.rpg.AurivaleProtocolLib;

public class HideEntityDamageAnimation extends PacketAdapter {
    public HideEntityDamageAnimation(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.DAMAGE_EVENT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        event.setCancelled(true);
    }
}
