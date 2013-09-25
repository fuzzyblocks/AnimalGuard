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
package net.fuzzyblocks.animalguard.util;

import net.fuzzyblocks.animalguard.AnimalGuard;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MessagesManager {

    private final AnimalGuard instance;
    private YamlConfiguration messages;
    private File messagesFile;

    public MessagesManager(AnimalGuard plugin) {
        instance = plugin;
        saveDefaultMessages();
    }

    public Map<String, String> getMessages() {
        if (messages == null)
            reloadMessages();
        Map<String, String> result = new HashMap<>();
        for (String s : messages.getKeys(false)) result.put(s,
                ChatColor.translateAlternateColorCodes('&', messages.getString(s)));
        return result;
    }

    private void reloadMessages() {
        if (messagesFile == null)
            messagesFile = new File(instance.getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        InputStream input = instance.getResource("messages.yml");
        if (input != null)
            messages.setDefaults(YamlConfiguration.loadConfiguration(input));
    }

    private void saveDefaultMessages() {
        if (messagesFile == null)
            messagesFile = new File(instance.getDataFolder(), "messages.yml");
        if (!messagesFile.exists())
            instance.saveResource("messages.yml", false);
    }
}
