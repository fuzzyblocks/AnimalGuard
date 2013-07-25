package net.fuzzyblocks.animalguard.listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    public static AnimalGuard plugin;
    String cannotKillMobs = ChatColor.DARK_RED + "You cannot attack mobs here!";

    public DamageListener(AnimalGuard instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttacked(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Entity entity = e.getEntity();
            Player player = (Player) e.getDamager();
            if (AnimalGuard.isProtectedFromPlayer(entity.getType())
                    && !WGBukkit.getPlugin().canBuild(player, entity.getLocation())) {
                e.setCancelled(true);
                player.sendMessage(cannotKillMobs);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttackByProjectile(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) e.getDamager();
            if (projectile.getShooter() instanceof Player) {
                Player player = (Player) projectile.getShooter();
                Entity entity = e.getEntity();

                if (AnimalGuard.isProtectedFromPlayer(entity.getType())
                        && !WGBukkit.getPlugin().canBuild(player, entity.getLocation())) {
                    e.setCancelled(true);
                    projectile.remove();
                    player.sendMessage(cannotKillMobs);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMonsterDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Monster)
            if (AnimalGuard.isProtectedFromMonster(e.getEntityType()))
                e.setCancelled(true);
    }
}
