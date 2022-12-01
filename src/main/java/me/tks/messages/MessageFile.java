package me.tks.messages;

import me.tks.playerwarp.PWarp;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageFile {
    private FileConfiguration messageFile;
    private File customConfigFile;

    /**
     * Creates the message file if it doesn't exist.
     * @param p reference to main
     */
    public void createMessageFile(PWarp p) {
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

    /**
     * Getter for the file configuration.
     * @return
     */
    FileConfiguration getMessageFile() {
        return this.messageFile;
    }

    /**
     * Checks if the message file contains a message for all paths.
     */
    public void checkConfig() {
        final PWarp p = PWarp.getPlugin(PWarp.class);
        this.customConfigFile = new File(p.getDataFolder(), "messages.yml");
        if (!this.customConfigFile.exists()) {
            return;
        }
        for (final Messages msg : Messages.values()) {
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
