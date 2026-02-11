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

public class Packet_SetSlot extends PacketAdapter {
    public Packet_SetSlot(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.SET_SLOT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
        PacketContainer container = event.getPacket();

        int windowid = container.getIntegers().read(0);
        int stateid = container.getIntegers().read(1);
        int slot = container.getIntegers().read(2);
        ItemStack stack = container.getItemModifier().read(0).clone();

        if(stack.getType() != Material.AIR) {
            StripMetaData.stripdata(stack);
        }

        PacketContainer out = new PacketContainer(PacketType.Play.Server.SET_SLOT);
        out.getIntegers().write(0, windowid);
        out.getIntegers().write(1, stateid);
        out.getIntegers().write(2, slot);
        out.getItemModifier().write(0, stack);

        event.setPacket(out);
    }
}
