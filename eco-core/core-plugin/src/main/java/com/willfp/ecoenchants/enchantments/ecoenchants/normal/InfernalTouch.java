package com.willfp.ecoenchants.enchantments.ecoenchants.normal;

import com.willfp.eco.util.drops.DropQueue;
import com.willfp.eco.util.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.tuplets.Pair;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class InfernalTouch extends EcoEnchant {
    private static final HashMap<Material, Pair<Material, Integer>> RECIPES = new HashMap<>();
    private static final Set<Material> FORTUNE_MATERIALS = new HashSet<>(
            Arrays.asList(
                    Material.GOLD_INGOT,
                    Material.IRON_INGOT
            )
    );

    public InfernalTouch() {
        super(
                "infernal_touch", EnchantmentType.NORMAL
        );
    }

    static {
        Iterator<Recipe> iterator = Bukkit.recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (!(recipe instanceof FurnaceRecipe)) {
                continue;
            }
            FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;
            int xp = (int) Math.ceil(furnaceRecipe.getExperience());
            RECIPES.put(furnaceRecipe.getInput().getType(), new Pair<>(furnaceRecipe.getResult().getType(), xp));
        }
    }

    @NotNull
    private static Pair<Material, Integer> getOutput(@NotNull final Material input) {
        Pair<Material, Integer> toReturn = RECIPES.get(input);
        if (toReturn == null) {
            return new Pair<>(input, 0);
        }
        return toReturn;
    }
    @EventHandler
    public void infernalTouchBreak(@NotNull final BlockDropItemEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (event.getBlock().getState() instanceof Container) {
            return;
        }

        if (event.isCancelled()) {
            return;
        }

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        if (this.getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        Collection<ItemStack> drops = new ArrayList<>();

        for (Item item : event.getItems()) {
            drops.add(item.getItemStack());
        }

        int experience = 0;
        int fortune = EnchantChecks.getMainhandLevel(player, Enchantment.LOOT_BONUS_BLOCKS);

        for (ItemStack itemStack : drops) {
            Pair<Material, Integer> out = getOutput(itemStack.getType());
            itemStack.setType(out.getFirst());
            experience += out.getSecond();

            if (fortune > 0 && FORTUNE_MATERIALS.contains(itemStack.getType())) {
                itemStack.setAmount((int) Math.ceil(1 / ((double) fortune + 2) + ((double) fortune + 1) / 2));
                experience++;
            }
        }

        event.getItems().clear();

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops)
                .addXP(experience)
                .push();
    }
}
