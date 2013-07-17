package net.fuzzyblocks.animalguard;

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

    public ShearListener(AnimalGuard instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSheepShear(PlayerShearEntityEvent event) {
        if (event.getEntity() instanceof Sheep) {
            if (event.isCancelled())
                return;
            Player player = event.getPlayer();
            Location loc = event.getEntity().getLocation();

            if (plugin.getConfig().getBoolean("shear-protect"))
                if (!this.plugin.getWorldGuardPlugin().canBuild(player, loc))
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot shear sheep here!");
        }
    }
}
