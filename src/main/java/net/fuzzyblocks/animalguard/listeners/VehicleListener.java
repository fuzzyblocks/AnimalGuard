/*
* Copyright (c) 2013, LankyLord
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
* * Redistributions of source code must retain the above copyright notice, this
* list of conditions and the following disclaimer.
* * Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*/
package net.fuzzyblocks.animalguard.listeners;

import com.sk89q.worldguard.bukkit.WGBukkit;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class VehicleListener implements Listener {
    private final String cannotEnterVehicle;
    private final String cannotLeaveVehicle;

    public VehicleListener(AnimalGuard instance) {
        cannotEnterVehicle = instance.getMessage("mob-enter-vehicle");
        cannotLeaveVehicle = instance.getMessage("mob-leave-vehicle");
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onVehicleEntry(VehicleEnterEvent e) {
        if (e.getVehicle() instanceof Animals) {
            Player player = (Player) e.getEntered();
            if (!WGBukkit.getPlugin().canBuild(player, e.getVehicle().getLocation())) {
                e.setCancelled(true);
                player.sendMessage(cannotEnterVehicle);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onVehicleExit(VehicleExitEvent e) {
        if (e.getVehicle() instanceof Animals) {
            Player player = (Player) e.getExited();
            if (!WGBukkit.getPlugin().canBuild(player, e.getVehicle().getLocation())) {
                e.setCancelled(true);
                player.sendMessage(cannotLeaveVehicle);
            }
        }
    }
}
