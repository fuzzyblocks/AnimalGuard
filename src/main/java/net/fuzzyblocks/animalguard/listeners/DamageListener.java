package net.fuzzyblocks.animalguard.listeners;

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
import net.fuzzyblocks.animalguard.AnimalGuard;

public class DamageListener implements Listener {

    String cannotKillMobs = ChatColor.DARK_RED + "You cannot attack mobs here!";
    public static AnimalGuard plugin;
    private List<EntityType> protectedFromPlayer = new ArrayList<>();
    private List<EntityType> protectedFromMonster = new ArrayList<>();

    public DamageListener(AnimalGuard instance) {
        plugin = instance;
        for (String entity : plugin.getConfig().getStringList("protect-from-player")) {
            protectedFromPlayer.add(EntityType.fromName(entity));
        }

        for (String entity : plugin.getConfig().getStringList("protect-from-monsters"))
            protectedFromMonster.add(EntityType.fromName(entity));

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttacked(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            EntityType entity = e.getEntityType();
            Player player = (Player) e.getDamager();
            Location loc = e.getEntity().getLocation();
            if (protectedFromPlayer.contains(entity))
                if (plugin.getWorldGuardPlugin().canBuild(player, loc)) {
                    e.setCancelled(false);
                } else {
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

                if (protectedFromPlayer.contains(entity)) {
                    Player player = (Player) ((Projectile) e.getDamager()).getShooter();
                    Location loc = e.getEntity().getLocation();
                    if (plugin.getWorldGuardPlugin().canBuild(player, loc)) {
                        e.setCancelled(false);
                    } else {
                        e.setCancelled(true);
                        player.sendMessage(cannotKillMobs);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMonsterDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Monster)
            if (protectedFromMonster.contains(e.getEntityType().toString()))
                e.setCancelled(true);
    }
}
