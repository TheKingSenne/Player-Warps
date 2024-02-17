package me.tks.playerwarp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class DummyPlayer implements OfflinePlayer {

    // fixes an issue in newer minecraft versions where usernames are expected for skulls
    private static final String EMPTY_USERNAME = "";
    private final UUID uuid;
    private final String name;

    public DummyPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public String getName() {
        if (name == null) {
            return EMPTY_USERNAME;
        }
        return name;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public boolean isBanned() {
        return false;
    }

    @Override
    public boolean isWhitelisted() {
        return false;
    }

    @Override
    public void setWhitelisted(boolean value) {
        // ignored
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public long getFirstPlayed() {
        return 0;
    }

    @Override
    public long getLastPlayed() {
        return 0;
    }

    @Override
    public boolean hasPlayedBefore() {
        return false;
    }

    @Override
    public Location getBedSpawnLocation() {
        return null;
    }

    @Override
    public void incrementStatistic(Statistic statistic) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void decrementStatistic(Statistic statistic) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public int getStatistic(Statistic statistic) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public void incrementStatistic(Statistic statistic, EntityType entityType, int amount) throws IllegalArgumentException {
        // ignored
    }

    @Override
    public void decrementStatistic(Statistic statistic, EntityType entityType, int amount) {
        // ignored
    }

    @Override
    public void setStatistic(Statistic statistic, EntityType entityType, int newValue) {
        // ignored
    }

    @Override
    public Map<String, Object> serialize() {
        return Collections.emptyMap();
    }

    @Override
    public boolean isOp() {
        return false;
    }

    @Override
    public void setOp(boolean value) {
        // ignored
    }
}
