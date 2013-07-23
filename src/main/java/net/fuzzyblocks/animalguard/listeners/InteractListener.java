package net.fuzzyblocks.animalguard.listeners;

import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
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
            Location loc = e.getEntity().getLocation();
            if (!this.plugin.getWorldGuardPlugin().canBuild(player, loc))
                e.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You cannot shear sheep here!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSheepDye(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        Location loc = entity.getLocation();
        ItemStack item = player.getItemInHand();
        if (entity instanceof Sheep && item.getTypeId() == 351
                && !plugin.getWorldGuardPlugin().canBuild(player, loc)) {
            e.setCancelled(true);
            DyeColor dyeColor = ((Sheep) entity).getColor();
            if (dyeColor == DyeColor.WHITE)
                ((Sheep) entity).setColor(DyeColor.SILVER);
            ((Sheep) entity).setColor(DyeColor.WHITE);
            ((Sheep) entity).setColor(dyeColor);
            player.sendMessage(ChatColor.DARK_RED + "You cannot dye sheep here!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMobLeash(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        Location loc = entity.getLocation();
        ItemStack item = player.getItemInHand();
        if ((item.getTypeId() == 420) && !plugin.getWorldGuardPlugin().canBuild(player, loc) && plugin.protectedFromPlayer.contains(entity.getType())) {
            e.setCancelled(true);
            player.sendMessage(ChatColor.DARK_RED + "You cannot leash mobs here!");
        }
    }
}
