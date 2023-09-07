package xyz.distemi.prtp.data;

import org.jetbrains.annotations.Nullable;
import xyz.distemi.prtp.data.calculator.IRTPYCalculator;

import java.util.List;

public class Settings {
    public static String defaultCommand;
    public static List<String> ignoredBlocks;
    public static List<String> preventBlocks;

    public static int maxTries;
    public static boolean calculateSync = false;
    @Nullable
    public static IRTPYCalculator yCalculator;
}
