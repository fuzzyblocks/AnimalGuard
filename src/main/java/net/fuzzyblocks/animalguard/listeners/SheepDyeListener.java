/*
* Copyright (c) 2013 cedeel.
* All rights reserved.
*
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* * Redistributions of source code must retain the above copyright
* notice, this list of conditions and the following disclaimer.
* * Redistributions in binary form must reproduce the above copyright
* notice, this list of conditions and the following disclaimer in the
* documentation and/or other materials provided with the distribution.
* * The name of the author may not be used to endorse or promote products
* derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
* ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
* LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
* SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package net.fuzzyblocks.animalguard.listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class SheepDyeListener implements Listener {

    private final String sheepDyeString;

    public SheepDyeListener(AnimalGuard instance) {
        sheepDyeString = instance.getMessage("sheep-dye");
    }

    @EventHandler(ignoreCancelled = true)
    public void onSheepDye(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.SHEEP) {
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

                player.sendMessage(sheepDyeString);
            }
        }
    }
}
