package com.willfp.ecoenchants.enchantments.ecoenchants.curse;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchantBuilder;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.util.NumberUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.util.Vector;

public class InaccuracyCurse extends EcoEnchant {
    public InaccuracyCurse() {
        super(
                new EcoEnchantBuilder("inaccuracy_curse", EnchantmentType.CURSE,5.0)
        );
    }

    // START OF LISTENERS


    @Override
    public void onBowShoot(LivingEntity shooter, int level, EntityShootBowEvent event) {
        double spread = this.getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "spread");

        Vector velocity = event.getProjectile().getVelocity().clone();

        velocity.add(new Vector(NumberUtils.randFloat(-spread, spread), NumberUtils.randFloat(-spread, spread), NumberUtils.randFloat(-spread, spread)));
        event.getProjectile().setVelocity(velocity);
    }
}
