package net.fuzzyblocks.animalguard.listeners;

import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.ArrayList;
import java.util.List;

public class DamageListener implements Listener {

    public static AnimalGuard plugin;
    String cannotKillMobs = ChatColor.DARK_RED + "You cannot attack mobs here!";

    public DamageListener(AnimalGuard instance) {
        plugin = instance;
        for (String entity : plugin.getConfig().getStringList("protect-from-player")) {
            plugin.protectedFromPlayer.add(EntityType.fromName(entity));
        }

        for (String entity : plugin.getConfig().getStringList("protect-from-monsters"))
            plugin.protectedFromMonster.add(EntityType.fromName(entity));

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttacked(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            EntityType entity = e.getEntityType();
            Player player = (Player) e.getDamager();
            Location loc = e.getEntity().getLocation();
            if (plugin.protectedFromPlayer.contains(entity))
                if (!plugin.getWorldGuardPlugin().canBuild(player, loc)) {
                    e.setCancelled(true);
                    player.sendMessage(cannotKillMobs);
                }
        }

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttackByProjectile(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Projectile) {
            if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                EntityType entity = e.getEntity().getType();

                if (plugin.protectedFromPlayer.contains(entity)) {
                    Player player = (Player) ((Projectile) e.getDamager()).getShooter();
                    Location loc = e.getEntity().getLocation();
                    if (!plugin.getWorldGuardPlugin().canBuild(player, loc)) {
                        e.setCancelled(true);
                        e.getDamager().remove();
                        player.sendMessage(cannotKillMobs);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMonsterDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Monster)
            if (plugin.protectedFromMonster.contains(e.getEntityType().toString()))
                e.setCancelled(true);
    }
}
