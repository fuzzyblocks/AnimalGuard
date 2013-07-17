package net.fuzzyblocks.animalguard;

// import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;

public class DamageListener implements Listener {

    String cannotKillMobs = ChatColor.DARK_RED + "You cannot attack mobs here!";
    public static AnimalGuard plugin;
    public boolean debug = plugin.getConfig().getBoolean("debug");
    private List<String> protectFromPlayer = plugin.getConfig().getStringList("protect-from-player");
    private List<String> protectFromMonster = plugin.getConfig().getStringList("protect-from-monsters");

    public DamageListener(AnimalGuard instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttacked(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            String entity = e.getEntityType().toString();
            Player player = (Player) e.getDamager();
            Location loc = e.getEntity().getLocation();
            if (debug)
                player.sendMessage(entity);
            if (protectFromPlayer.contains(entity))
                if (plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")) {
                    e.setCancelled(false);
                    if (debug)
                        sendDebugMessage(player, false, e.getDamager().getType().toString(), e.getEntity().getType().toString());
                } else {
                    e.setCancelled(true);
                    player.sendMessage(cannotKillMobs);
                    if (debug)
                        sendDebugMessage(player, true, e.getDamager().getType().toString(), e.getEntity().getType().toString());
                }
        }

    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onAttackArrow(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow) {
            Projectile arrow = (Arrow) e.getDamager();
            String entity = e.getEntity().getType().toString();
            if (arrow.getShooter() instanceof Player && protectFromPlayer.contains(entity)) {
                Player player = (Player) arrow.getShooter();
                Location loc = e.getEntity().getLocation();
                if (plugin.getWorldGuardPlugin().canBuild(player, loc) || player.hasPermission("animalprotect.bypass")) {
                    e.setCancelled(false);
                    if (debug)
                        sendDebugMessage(player, false, e.getDamager().getType().toString(), e.getEntity().getType().toString());
                } else {
                    e.setCancelled(true);
                    player.sendMessage(cannotKillMobs);
                    if (debug)
                        sendDebugMessage(player, true, e.getDamager().getType().toString(), e.getEntity().getType().toString());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onMonsterDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Monster)
            if (protectFromMonster.contains(e.getEntityType().toString()))
                e.setCancelled(true);
    }

    private void sendDebugMessage(Player p, Boolean fail, String damager, String victim) {
        p.sendMessage(damager + " attacked " + victim);
        if (fail)
            p.sendMessage("Attack failed");
        else
            p.sendMessage("Attack successful");
    }
}
