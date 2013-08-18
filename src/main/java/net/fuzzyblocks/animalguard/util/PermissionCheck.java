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
package net.fuzzyblocks.animalguard.util;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;

public class PermissionCheck {

    private static boolean allowPvpTameable;

    public PermissionCheck(boolean allowPvpTameable) {
        this.allowPvpTameable = allowPvpTameable;
    }

    /**
     * Check whether damage should be dealt
     *
     * @param player The player attempting to deal damage
     * @param entity The entity being attacked
     * @return Whether the damage should be dealt
     */
    public static boolean blockDamage(Player player, Entity entity) {
        boolean result = false;
        ApplicableRegionSet ars = WGBukkit.getRegionManager(entity.getWorld()).getApplicableRegions(entity.getLocation());
        if (AnimalGuard.isProtectedFromPlayer(entity.getType())
                && !WGBukkit.getPlugin().canBuild(player, entity.getLocation()) && !ars.allows(AnimalGuard.ANIMAL_GUARD)) {
            result = true;
        }

        if (entity instanceof Tameable && allowPvpTameable) {
            // Should *not* block if PvP is allowed.
            result = !ars.allows(DefaultFlag.PVP);
        }
        return result;
    }

    /**
     * Check whether interaction should be blocked
     *
     * @param player The player attempting to interact
     * @param entity The entity being interacted with
     * @return Whether the interaction should be blocked.
     */
    public static boolean blockInteract(Player player, Entity entity) {
        boolean result = false;
        ApplicableRegionSet ars = WGBukkit.getRegionManager(entity.getWorld()).getApplicableRegions(entity.getLocation());
        if (!WGBukkit.getPlugin().canBuild(player, entity.getLocation()) && !ars.allows(AnimalGuard.ANIMAL_GUARD)) {
            result = true;
        }
        return result;
    }
}
