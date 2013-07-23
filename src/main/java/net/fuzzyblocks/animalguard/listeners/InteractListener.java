package net.fuzzyblocks.animalguard.listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    private AnimalGuard plugin;

    public InteractListener(AnimalGuard instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSheepShear(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep) {
            Player player = e.getPlayer();
            if (!WGBukkit.getPlugin().canBuild(player, e.getEntity().getLocation()))
                e.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You cannot shear sheep here!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSheepDye(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Sheep) {
            Sheep sheep = (Sheep)e.getRightClicked();
            Player player = e.getPlayer();
            ItemStack item = player.getItemInHand();
            if (item.getType() == Material.INK_SACK
                    && !WGBukkit.getPlugin().canBuild(player, sheep.getLocation())) {

                // Cancel dye
                e.setCancelled(true);
                DyeColor dyeColor = sheep.getColor();
                if (dyeColor == DyeColor.WHITE)
                    sheep.setColor(DyeColor.SILVER);
                sheep.setColor(DyeColor.WHITE);
                sheep.setColor(dyeColor);

                // Give player back his dye if different from the colour of the sheep
                DyeColor playerDye = DyeColor.getByDyeData(item.getData().getData());
                if (playerDye != dyeColor) {
                    ItemStack dye = new ItemStack(Material.INK_SACK, playerDye.getDyeData());
                    player.getInventory().addItem(dye);
                    //noinspection deprecation
                    player.updateInventory();
                }

                player.sendMessage(ChatColor.DARK_RED + "You cannot dye sheep here!");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMobLeash(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        ItemStack item = player.getItemInHand();
        if ((item.getType() == Material.LEASH)
                && !WGBukkit.getPlugin().canBuild(player, entity.getLocation())
                && plugin.protectedFromPlayer.contains(entity.getType())) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You cannot leash mobs here!");
        }
    }
}
