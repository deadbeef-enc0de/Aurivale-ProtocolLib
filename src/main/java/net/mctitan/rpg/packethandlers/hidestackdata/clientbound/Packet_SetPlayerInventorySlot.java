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

public class Packet_SetPlayerInventorySlot extends PacketAdapter {
    public Packet_SetPlayerInventorySlot(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.SET_PLAYER_INVENTORY);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
        PacketContainer container = event.getPacket();

        int slot =  container.getIntegers().read(0);
        ItemStack stack = container.getItemModifier().read(0).clone();

        if(stack.getType() != Material.AIR) {
            StripMetaData.stripdata(stack);
        }

        PacketContainer out = new PacketContainer(PacketType.Play.Server.SET_PLAYER_INVENTORY);
        out.getIntegers().write(0, slot);
        out.getItemModifier().write(0, stack);

        event.setPacket(out);
    }
}
