package xyz.distemi.prtp.data.calculator;

import org.bukkit.World;

import java.util.function.Consumer;

public class RTPCoreYCalculator implements IRTPYCalculator {
    @Override
    public void calculateY(World world, int x, int z, Consumer<Integer> consumer) {
        consumer.accept(world.getHighestBlockYAt(x, z));
    }
}
