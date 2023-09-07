package xyz.distemi.prtp.data.antimations;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class EmptyAnimation implements IProfileAnimation {

    @Override
    public void parse(String arg) {
    }

    @Override
    public void process(Player player, Location to) {
        player.teleport(to.toBlockLocation());
    }
}
