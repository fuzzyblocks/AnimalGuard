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

    String cannotShearSheep = ChatColor.DARK_RED + "You cannot shear sheep here!";
    String cannotDyeSheep = ChatColor.DARK_RED + "You cannot dye sheep here!";
    private AnimalGuard plugin;

    public InteractListener(AnimalGuard instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSheepShear(PlayerShearEntityEvent event) {
        if (event.getEntity() instanceof Sheep) {
            Player player = event.getPlayer();
            Location loc = event.getEntity().getLocation();
            if (!this.plugin.getWorldGuardPlugin().canBuild(player, loc))
                event.setCancelled(true);
            player.sendMessage(cannotShearSheep);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSheepDye(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity en = event.getRightClicked();
        Location loc = en.getLocation();
        ItemStack item = event.getPlayer().getItemInHand();
        if (en instanceof Sheep && item.getTypeId() == 351 && !plugin.getWorldGuardPlugin().canBuild(player, loc)) {
            event.setCancelled(true);
            DyeColor dyeColor = ((Sheep) en).getColor();
            ((Sheep) en).setColor(DyeColor.WHITE);
            ((Sheep) en).setColor(dyeColor);
            player.sendMessage(cannotDyeSheep);
        }
        if (item.getTypeId() == 420 && !plugin.getWorldGuardPlugin().canBuild(player, loc)) {
            event.setCancelled(true);
        }
    }
}
