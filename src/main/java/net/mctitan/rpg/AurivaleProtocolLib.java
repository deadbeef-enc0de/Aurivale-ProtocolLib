package net.mctitan.rpg;

import net.mctitan.rpg.handlers.*;
import net.mctitan.rpg.packethandlers.*;
import net.mctitan.rpg.packethandlers.hidestackdata.clientbound.*;
import net.mctitan.rpg.packethandlers.hidestackdata.serverbound.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;

public class AurivaleProtocolLib extends JavaPlugin {
    private static AurivaleProtocolLib instance;
    private ProtocolManager protocol;

    @Override
    public void onEnable() {
        // store the instance
        instance = this;

        // add event listeners
        Bukkit.getPluginManager().registerEvents(PlayerStatScoreboard.instance(), this);

        // add the packet handlers
        protocol = ProtocolLibrary.getProtocolManager();
        protocol.addPacketListener(new HideEntityDamageAnimation(ListenerPriority.NORMAL));
        protocol.addPacketListener(new HideElderGaurdianEffect(ListenerPriority.NORMAL));
        protocol.addPacketListener(new InvisibleEquipment(ListenerPriority.NORMAL));

        // hide item stacks
        protocol.addPacketListener(new Packet_EntityEquipment(ListenerPriority.NORMAL));
        protocol.addPacketListener(new Packet_OpenMerchantWindow(ListenerPriority.NORMAL));
        protocol.addPacketListener(new Packet_SetCursorItem(ListenerPriority.NORMAL));
        protocol.addPacketListener(new Packet_SetPlayerInventorySlot(ListenerPriority.NORMAL));
        protocol.addPacketListener(new Packet_SetSlot(ListenerPriority.NORMAL));
        protocol.addPacketListener(new Packet_WindowItems(ListenerPriority.NORMAL));

        protocol.addPacketListener(new Packet_WindowClick(ListenerPriority.NORMAL));
    }

    @Override
    public void onDisable() {
        // remove instance
        instance = null;
    }

    public static AurivaleProtocolLib instance() { return instance; }
    public ProtocolManager protocol() {  return protocol; }
}
