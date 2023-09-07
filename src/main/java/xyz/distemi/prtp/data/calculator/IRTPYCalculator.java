package xyz.distemi.prtp.data.calculator;

import org.bukkit.World;

import java.util.function.Consumer;

@FunctionalInterface
public interface IRTPYCalculator {
    void calculateY(World world, int x, int z, Consumer<Integer> consumer);
}
