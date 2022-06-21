package xyz.distemi.prtp;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class PUtils {
    private static final BukkitScheduler bukkitScheduler = Bukkit.getScheduler();

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

    public static int usys(Runnable a) {
        return bukkitScheduler.runTask(PRTP.THIS, a).getTaskId();
    }

    public static void uasy(Runnable a) {
        bukkitScheduler.runTaskAsynchronously(PRTP.THIS, a);
    }

    public static void usysaw(Runnable a) {
        int t = usys(a);
        while (bukkitScheduler.isQueued(t) || bukkitScheduler.isCurrentlyRunning(t)) {
            try {
                Thread.sleep(1);
            } catch (Exception ignored) {
            }
        }
    }
}
