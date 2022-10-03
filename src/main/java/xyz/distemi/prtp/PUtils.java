package xyz.distemi.prtp;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import xyz.distemi.prtp.data.Settings;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

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

    public static int calcY(World world, int x, int z) throws Exception {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        AtomicReference<Chunk> chunk = new AtomicReference<>(null);
        if (Settings.calculateSync) {
            scheduler.runTask(PRTP.THIS, () -> chunk.set(world.getChunkAt(x, z)));
        } else {
            world.getChunkAtAsync(x, z, chunk::set);
        }
        while (chunk.get() == null) {
            try {
                Thread.sleep(1);
            } catch (Exception ignored) {
            }
        }
        Chunk sameChunk = chunk.get();
        int xOffset = x - sameChunk.getX();
        int zOffset = z - sameChunk.getZ();

        for (int y = 255; y > 0; y--) {
            try {
                Block block = sameChunk.getBlock(xOffset, y, zOffset);
                Material material = block.getType();
                String mat_name = material.name();
                if (Settings.preventBlocks.contains(mat_name)) {
                    return -1;
                }
                if (material.isTransparent() || Settings.ignoredBlocks.contains(mat_name)) {
                    continue;
                }
                return block.getY() + 1;
            } catch (Exception ignored) {
            }
        }
        return -1;
    }
}
