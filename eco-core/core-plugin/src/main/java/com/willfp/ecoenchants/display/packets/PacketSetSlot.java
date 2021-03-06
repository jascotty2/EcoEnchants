package com.willfp.ecoenchants.display.packets;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;
import com.willfp.eco.util.protocollib.AbstractPacketAdapter;
import com.willfp.ecoenchants.display.EnchantDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

public class PacketSetSlot extends AbstractPacketAdapter {
    /**
     * Instantiate a new listener for {@link PacketType.Play.Server#SET_SLOT}.
     *
     * @param plugin The plugin to listen through.
     */
    public PacketSetSlot(@NotNull final AbstractEcoPlugin plugin) {
        super(plugin, PacketType.Play.Server.SET_SLOT, false);
    }

    @Override
    public void onSend(@NotNull final PacketContainer packet,
                       @NotNull final Player player) {
        packet.getItemModifier().modify(0, item -> {
            boolean hideEnchants = false;

            if (item == null) {
                return null;
            }

            if (item.getItemMeta() != null) {
                hideEnchants = item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS);
            }

            item = EnchantDisplay.displayEnchantments(item, hideEnchants);
            return item;
        });
    }
}
