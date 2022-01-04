package com.levelmc.core.api.utils;

import com.levelmc.core.api.world.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Set;

public class SoundUtils {
    /**
     * Plays a sound to the player specified at full pitch & speed
     *
     * @param player
     * @param sound
     */
    public static void playSound(Player player, Sound sound) {
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }

    /**
     * Plays a sound for the player with the given volume and pitch
     *
     * @param player
     * @param sound
     * @param volume
     * @param pitch
     */
    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

    /**
     * Plays a sound for the player at a location which isn't their current location
     *
     * @param player
     * @param location
     * @param sound
     */
    public static void playSoundDistant(Player player, Location location, Sound sound) {
        player.playSound(location, sound, 1.0f, 1.0f);
    }

    /**
     * Plays a sound for the player at a location other than their current location
     * with the given volume and pitch
     *
     * @param player
     * @param location
     * @param sound
     * @param volume
     * @param pitch
     */
    public static void playSoundDistant(Player player, Location location, Sound sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }

    /**
     * Plays a sound for the player at a location which isn't their location with a given volume and pitch
     *
     * @param player
     * @param location
     * @param sound
     * @param volume
     * @param pitch
     */
    public static void playSoundForPlayerAtLocation(Player player, Location location, Sound sound, float volume, float pitch) {
        player.playSound(location, sound, volume, pitch);
    }

    /**
     * Plays a sound for all players at a location
     *
     * @param location
     * @param sound
     * @param volume
     * @param pitch
     */
    public static void playSoundForPlayersAtLocation(Location location, Sound sound, float volume, float pitch) {
        location.getWorld().playSound(location, sound, volume, pitch);
    }

    /**
     * Plays a sound for all players in the given areas radius, at a location other than the area center
     *
     * @param areaLocation
     * @param soundPlayLocation
     * @param radius
     * @param sound
     * @param volume
     * @param pitch
     */
    public static void playSoundDistantAtLocation(Location areaLocation, Location soundPlayLocation, double radius, Sound sound, float volume, float pitch) {
        Set<Player> playersInLocation = LocationUtils.getPlayersInRadius(areaLocation, radius);
        if (playersInLocation.size() == 0) {
            return;
        }

        for (Player player : playersInLocation) {
            playSoundDistant(player, soundPlayLocation, sound, volume, pitch);
        }
    }
}
