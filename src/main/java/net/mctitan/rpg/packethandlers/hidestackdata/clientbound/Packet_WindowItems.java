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

import java.util.LinkedList;
import java.util.List;

public class Packet_WindowItems extends PacketAdapter {
    public Packet_WindowItems(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.WINDOW_ITEMS);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
        PacketContainer container = event.getPacket();

        int windowid = container.getIntegers().read(0);
        int stateid = container.getIntegers().read(1);
        List<ItemStack> items = container.getItemListModifier().read(0);
        ItemStack inhand = container.getItemModifier().read(0).clone();

        List<ItemStack> newitems = new LinkedList<>();
        for(ItemStack stack : items) {
            stack = stack.clone();
            if(stack.getType() != Material.AIR) {
                StripMetaData.stripdata(stack);
            }
            newitems.add(stack);
        }

        if(inhand.getType() != Material.AIR) {
            StripMetaData.stripdata(inhand);
        }

        PacketContainer out = new PacketContainer(PacketType.Play.Server.WINDOW_ITEMS);
        out.getIntegers().write(0, windowid);
        out.getIntegers().write(1, stateid);
        out.getItemListModifier().write(0, newitems);
        out.getItemModifier().write(0, inhand);

        event.setPacket(out);
    }
}
