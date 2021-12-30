package xyz.distemi.prtp;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class PUtils {
    public static String a(@NotNull String text, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            text = PlaceholderAPI.setPlaceholders(player, text);
        }
        return b(text);
    }

    public static String b(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static int r(int mi, int ma) {
        return ThreadLocalRandom.current().nextInt(mi, ma);
    }
}
