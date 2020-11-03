package me.tks.messages;

import me.tks.playerwarp.PWarp;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageFile {
    private FileConfiguration messageFile;
    private File customConfigFile;

    public void createMessageFile(final PWarp p) {
        this.customConfigFile = new File(p.getDataFolder(), "messages.yml");
        if (!this.customConfigFile.exists()) {
            this.customConfigFile.getParentFile().mkdirs();
            p.saveResource("messages.yml", false);
        }
        this.messageFile = new YamlConfiguration();
        try {
            this.messageFile.load(this.customConfigFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    FileConfiguration getMessageFile() {
        return this.messageFile;
    }

    public void reloadMessages() {
        this.messageFile = YamlConfiguration.loadConfiguration(this.customConfigFile);
    }

    public void checkConfig() {
        final PWarp p = (PWarp) PWarp.getPlugin((Class)PWarp.class);
        this.customConfigFile = new File(p.getDataFolder(), "messages.yml");
        if (!this.customConfigFile.exists()) {
            return;
        }
        for (final MessagePathAndDefault msg : MessagePathAndDefault.values()) {
            if (this.messageFile.get(msg.getPath()) == null) {
                FileConfiguration.createPath(this.messageFile, msg.getPath());
                this.messageFile.set(msg.getPath(), msg.getDefaultMessage());
                try {
                    this.messageFile.save(this.customConfigFile);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
