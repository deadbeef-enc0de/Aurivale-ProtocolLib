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
import org.bukkit.inventory.MerchantRecipe;

import java.util.LinkedList;
import java.util.List;

public class Packet_OpenMerchantWindow extends PacketAdapter {
    public Packet_OpenMerchantWindow(ListenerPriority priority) {
        super(AurivaleProtocolLib.instance(), priority, PacketType.Play.Server.OPEN_WINDOW_MERCHANT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if(event.getPlayer().getGameMode() == GameMode.CREATIVE) { return; }
        PacketContainer container = event.getPacket();

        int windowid = container.getIntegers().read(0);
        List<MerchantRecipe> recipes = container.getMerchantRecipeLists().read(0);
        int villagerlevel = container.getIntegers().read(1);
        int experience = container.getIntegers().read(2);
        boolean regular = container.getBooleans().read(0);
        boolean canrestock = container.getBooleans().read(1);

        List<MerchantRecipe> newrecipes = new LinkedList<>();
        for(MerchantRecipe recipe : recipes) {
            // get result
            ItemStack result = recipe.getResult().clone();
            StripMetaData.stripdata(result);

            // make recipe
            MerchantRecipe newrecipe = new MerchantRecipe(result, 10);

            // add ingredients
            for(ItemStack input : recipe.getIngredients()) {
                input = input.clone();
                if(input.getType() == Material.AIR) { continue; }
                StripMetaData.stripdata(input);
                newrecipe.addIngredient(input);
            }

            // add recipe to new list
            newrecipes.add(newrecipe);
        }

        PacketContainer out = new PacketContainer(PacketType.Play.Server.OPEN_WINDOW_MERCHANT);
        out.getIntegers().write(0, windowid);
        out.getMerchantRecipeLists().write(0, newrecipes);
        out.getIntegers().write(1, villagerlevel);
        out.getIntegers().write(2, experience);
        out.getBooleans().write(0, regular);
        out.getBooleans().write(1, canrestock);

        event.setPacket(out);
    }
}
