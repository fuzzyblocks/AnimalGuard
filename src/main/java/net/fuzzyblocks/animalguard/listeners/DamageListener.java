package net.fuzzyblocks.animalguard.listeners;

import net.fuzzyblocks.animalguard.AnimalGuard;
import net.fuzzyblocks.animalguard.util.PermissionCheck;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private static AnimalGuard plugin;
    private String cannotKillMobs;

    public DamageListener(AnimalGuard instance) {
        plugin = instance;
        cannotKillMobs = instance.getMessage("mob-attack");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttacked(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Entity entity = e.getEntity();
            Player player = (Player) e.getDamager();

            if (PermissionCheck.blockDamage(player, entity)) {
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

                if (PermissionCheck.blockDamage(player, entity)) {
                    e.setCancelled(true);
                    // Remove the projectile to prevent a glitch where the user gets spammed
                    // and this event re-run until the chunk is unloaded.
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
