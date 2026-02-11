package net.mctitan.rpg.packethandlers.hidestackdata.clientbound;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import net.mctitan.rpg.AurivaleProtocolLib;
import net.mctitan.rpg.packethandlers.hidestackdata.StripMetaData;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class Packet_EntityEquipment extends PacketAdapter {
    public Packet_EntityEquipment(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.ENTITY_EQUIPMENT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
        PacketContainer container = event.getPacket();

        int eid = container.getIntegers().read(0);
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> outgoing = new LinkedList<>();
        List<Pair<EnumWrappers.ItemSlot, org.bukkit.inventory.ItemStack>> incoming = container.getSlotStackPairLists().read(0);
        for(Pair<EnumWrappers.ItemSlot,org.bukkit.inventory.ItemStack> entry : incoming) {
            net.mctitan.rpg.data.ItemStack stack = new net.mctitan.rpg.data.ItemStack(entry.getSecond().clone());

            if(stack.bukkitstack().getType() != Material.AIR) {
                StripMetaData.stripdata(stack.bukkitstack());
            }
            outgoing.add(new Pair<>(entry.getFirst(), stack.bukkitstack()));
        }

        if(outgoing.size() == incoming.size()) {
            return;
        }

        PacketContainer out = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        out.getIntegers().write(0, eid);
        out.getSlotStackPairLists().write(0, outgoing);

        event.setPacket(out);
    }
}
