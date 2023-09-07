package xyz.distemi.prtp;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.distemi.prtp.data.Settings;

import java.util.SplittableRandom;
import java.util.function.Consumer;

public class PUtils {
    private static final SplittableRandom random = new SplittableRandom();

    public static String a(@NotNull String text, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            text = PlaceholderAPI.setPlaceholders(player, text);
        }
        return b(text);
    }

    public static String b(@NotNull String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static int r(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static void calcY(World world, int x, int z, Consumer<Integer> consumer) {
        if (Settings.yCalculator != null) {
            Settings.yCalculator.calculateY(world, x, z, consumer);
        } else {
            consumer.accept(world.getHighestBlockYAt(x, z));
        }
    }
}
