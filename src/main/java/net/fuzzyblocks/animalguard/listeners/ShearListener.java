package net.fuzzyblocks.animalguard.listeners;

import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class ShearListener implements Listener {

    private AnimalGuard plugin;
    String cannotShearSheep = ChatColor.DARK_RED + "You cannot shear sheep here!";

    public ShearListener(AnimalGuard instance) {
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
}
