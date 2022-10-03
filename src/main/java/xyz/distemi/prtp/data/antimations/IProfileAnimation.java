package xyz.distemi.prtp.data.antimations;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IProfileAnimation {
    void parse(String arg);

    void process(Player player, Location to);
}
