package net.mctitan.rpg.packethandlers;

import net.mctitan.rpg.AurivaleProtocolLib;
import net.mctitan.rpg.data.ItemStack;
import net.mctitan.rpg.enums.EffectType;
import net.mctitan.rpg.enums.StatusType;
import net.mctitan.rpg.modifier.Modifier;
import net.mctitan.rpg.modifier.types.StatusModifier;

import org.bukkit.Material;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;
import com.comphenix.protocol.wrappers.Pair;

import java.util.LinkedList;
import java.util.List;

public class InvisibleEquipment extends PacketAdapter {
    public InvisibleEquipment(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.ENTITY_EQUIPMENT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer container = event.getPacket();

        int eid = container.getIntegers().read(0);
        List<Pair<ItemSlot, org.bukkit.inventory.ItemStack>> outgoing = new LinkedList<>();
        List<Pair<ItemSlot, org.bukkit.inventory.ItemStack>> incoming = container.getSlotStackPairLists().read(0);
        for(Pair<ItemSlot,org.bukkit.inventory.ItemStack> entry : incoming) {
            ItemStack stack = new ItemStack(entry.getSecond().clone());

            boolean visible = true;
            for(Modifier modifier : stack.modifiers()) {
                if(modifier instanceof StatusModifier status) {
                    if(status.statustype() == StatusType.PERMANENT && status.effecttype() == EffectType.INVISIBILITY) {
                        visible = false;
                        break;
                    }
                }
            }

            if(visible) {
                outgoing.add(new Pair<>(entry.getFirst(), stack.bukkitstack()));
            }
        }

        if(outgoing.size() == incoming.size()) {
            return;
        }

        PacketContainer out = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
        if(outgoing.isEmpty()) {
            outgoing.add(new Pair(ItemSlot.MAINHAND, new org.bukkit.inventory.ItemStack(Material.AIR)));
        }
        out.getIntegers().write(0, eid);
        out.getSlotStackPairLists().write(0, outgoing);

        event.setPacket(out);
    }
}
