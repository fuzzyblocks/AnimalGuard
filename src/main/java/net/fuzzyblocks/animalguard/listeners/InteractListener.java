package net.fuzzyblocks.animalguard.listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

public class InteractListener implements Listener {

    private static final ChatColor MSG_COLOR = ChatColor.DARK_RED;
    // TODO: Make these strings configurable
    private static final String SHEEP_SHEAR = MSG_COLOR + "You cannot shear sheep here!";
    private static final String SHEEP_DYE   = MSG_COLOR + "You cannot dye sheep here!";
    private static final String MOB_LEASH   = MSG_COLOR + "You cannot leash mobs here!";
    private static final String COW_MILK    = MSG_COLOR + "You cannot milk cows here!";

    public InteractListener() {
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSheepShear(PlayerShearEntityEvent e) {
        if (e.getEntity() instanceof Sheep) {
            Player player = e.getPlayer();
            if (!WGBukkit.getPlugin().canBuild(player, e.getEntity().getLocation()))
                e.setCancelled(true);
            player.sendMessage(SHEEP_SHEAR);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSheepDye(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Sheep) {
            Sheep sheep = (Sheep)e.getRightClicked();
            Player player = e.getPlayer();
            ItemStack item = player.getItemInHand();
            if (item.getType() == Material.INK_SACK
                    && !WGBukkit.getPlugin().canBuild(player, sheep.getLocation())) {

                // Cancel dye
                e.setCancelled(true);
                DyeColor dyeColor = sheep.getColor();
                if (dyeColor == DyeColor.WHITE)
                    sheep.setColor(DyeColor.SILVER);
                sheep.setColor(DyeColor.WHITE);
                sheep.setColor(dyeColor);

                // Give player back his dye if different from the colour of the sheep
                DyeColor playerDye = DyeColor.getByDyeData(item.getData().getData());
                if (playerDye != dyeColor) {
                    ItemStack dye = new ItemStack(Material.INK_SACK, playerDye.getDyeData());
                    player.getInventory().addItem(dye);
                    //noinspection deprecation
                    player.updateInventory();
                }

                player.sendMessage(SHEEP_DYE);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMobLeash(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        ItemStack item = player.getItemInHand();
        if ((item.getType() == Material.LEASH)
                && !WGBukkit.getPlugin().canBuild(player, entity.getLocation())
                && AnimalGuard.isProtectedFromPlayer(entity.getType())) {
            e.setCancelled(true);
            player.sendMessage(MOB_LEASH);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCowMilk(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Cow) {
        Player player = e.getPlayer();
        Cow cow = (Cow) e.getRightClicked();
        ItemStack item = player.getItemInHand( );
            if ((item.getType() == Material.BUCKET)
                && !WGBukkit.getPlugin().canBuild(player, cow.getLocation())
                && AnimalGuard.isProtectedFromPlayer(EntityType.COW)) {
            e.setCancelled(true);
            player.sendMessage(COW_MILK);
            }
        }
    }
}
