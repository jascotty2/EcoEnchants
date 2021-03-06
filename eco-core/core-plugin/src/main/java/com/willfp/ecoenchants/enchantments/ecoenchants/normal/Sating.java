package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.enchantments.util.EnchantmentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class Sating extends EcoEnchant {
    public Sating() {
        super(
                "sating", EnchantmentType.NORMAL
        );
    }
    @EventHandler
    public void onSatingHunger(@NotNull final FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        if (!EnchantChecks.helmet(player, this)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        if (event.getFoodLevel() > player.getFoodLevel()) {
            return;
        }

        int level = EnchantChecks.getHelmetLevel(player, this);

        if (!EnchantmentUtils.passedChance(this, level)) {
            return;
        }

        event.setCancelled(true);
    }
}
