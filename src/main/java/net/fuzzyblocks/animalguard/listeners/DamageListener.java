package net.fuzzyblocks.animalguard.listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private static AnimalGuard plugin;
    private String cannotKillMobs;
    private boolean allowPvpTameable;

    public DamageListener(AnimalGuard instance, boolean tameablePvp) {
        plugin = instance;
        allowPvpTameable = tameablePvp;
        cannotKillMobs = instance.getMessage("mob-attack");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttacked(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Entity entity = e.getEntity();
            Player player = (Player) e.getDamager();

            if (blockDamage(player, entity)) {
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

                if (blockDamage(player, entity)) {
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

    /**
     * Check whether damage should be dealt
     *
     * @param player The player attempting to deal damage
     * @param entity The entity being attacked
     * @return Whether the damage should be dealt
     */
    private boolean blockDamage(Player player, Entity entity) {
        boolean result = false;
        if (AnimalGuard.isProtectedFromPlayer(entity.getType())
                && !WGBukkit.getPlugin().canBuild(player, entity.getLocation())) {
            result = true;
        }

        if (entity instanceof Tameable && allowPvpTameable) {
            // Should *not* block if PvP is allowed.
            ApplicableRegionSet ars = WGBukkit.getRegionManager(entity.getWorld()).getApplicableRegions(entity.getLocation());
            result = !ars.allows(DefaultFlag.PVP);
        }

        return result;
    }
}
