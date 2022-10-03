package xyz.distemi.prtp.data.antimations;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FallAnimation implements IProfileAnimation {
    public double height;
    public int preventDamageTicks;

    @Override
    public void parse(String arg) {
        String[] parts = arg.split(",");
        height = Double.parseDouble(parts[0]);
        preventDamageTicks = Integer.parseInt(parts[1]);
    }

    @Override
    public void process(Player player, Location to) {
        player.teleport(to.add(0, height, 0));
        player.setNoDamageTicks(preventDamageTicks);
    }
}
