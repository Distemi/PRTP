package xyz.distemi.prtp.data.calculator;

import org.bukkit.World;

import java.util.function.Consumer;

public class RTPCoreYCalculator implements IRTPYCalculator {
    @Override
    public void calculateY(World world, int x, int z, Consumer<Integer> consumer) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        world.getChunkAtAsync(chunkX, chunkZ, chunk -> consumer.accept(world.getHighestBlockYAt(x, z) + 1));
    }
}
