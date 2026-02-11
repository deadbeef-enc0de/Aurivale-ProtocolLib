package net.mctitan.rpg.packethandlers.hidestackdata.serverbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.mctitan.rpg.AurivaleProtocolLib;
import org.bukkit.GameMode;

public class Packet_WindowClick extends PacketAdapter {
    public Packet_WindowClick(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Client.WINDOW_CLICK);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
        PacketContainer container = event.getPacket();

        // we are writing -1 into the stateid to force a full inventory update
        container.getIntegers().write(1, -1);

        event.setPacket(container);
    }
}
