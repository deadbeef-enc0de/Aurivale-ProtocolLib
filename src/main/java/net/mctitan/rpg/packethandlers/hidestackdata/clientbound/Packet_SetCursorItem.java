package net.mctitan.rpg.packethandlers.hidestackdata.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.mctitan.rpg.AurivaleProtocolLib;
import net.mctitan.rpg.packethandlers.hidestackdata.StripMetaData;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Packet_SetCursorItem extends PacketAdapter {
    public Packet_SetCursorItem(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.SET_CURSOR_ITEM);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
        PacketContainer container = event.getPacket();

        ItemStack stack = container.getItemModifier().read(0).clone();
        if(stack.getType() != Material.AIR) {
            StripMetaData.stripdata(stack);
        }

        PacketContainer out = new PacketContainer(PacketType.Play.Server.SET_CURSOR_ITEM);
        out.getItemModifier().write(0, stack);

        event.setPacket(out);
    }
}
