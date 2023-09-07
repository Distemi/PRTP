package xyz.distemi.prtp.data.calculator;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import xyz.distemi.prtp.data.Settings;

import java.util.function.Consumer;

public class RTPPluginYCalculator implements IRTPYCalculator {
    private static void calcChunkySync(World world, int chunkX, int chunkY, Consumer<Chunk> consumer) {
        consumer.accept(world.getChunkAt(chunkX, chunkY));
    }

    private static void calcChunkyAsync(World world, int chunkX, int chunkY, Consumer<Chunk> consumer) {
        world.getChunkAtAsync(chunkX, chunkY, consumer::accept);
    }

    @Override
    public void calculateY(World world, int x, int z, Consumer<Integer> consumer) {
        int chunkX = x / 16;
        int chunkZ = z / 16;

        Consumer<Chunk> calculator = chunk -> {
            int xOffset = x - (chunk.getX() * 16);
            int zOffset = z - (chunk.getZ() * 16);

            for (int y = 255; y > 0; y--) {
                try {
                    Block block = chunk.getBlock(xOffset, y, zOffset);
                    Material material = block.getType();
                    String mat_name = material.name();
                    if (Settings.preventBlocks.contains(mat_name)) {
                        consumer.accept(-99999);
                        break;
                    }
                    if (material.isTransparent() || Settings.ignoredBlocks.contains(mat_name)) {
                        continue;
                    }
                    consumer.accept(block.getY() + 1);
                    break;
                } catch (Exception ignored) {
                }
            }
            consumer.accept(-99999);
        };


        if (Settings.calculateSync) {
            calcChunkySync(world, chunkX, chunkZ, calculator);
        } else {
            calcChunkyAsync(world, chunkX, chunkZ, calculator);
        }
    }
}
