package xyz.distemi.prtp.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import xyz.distemi.prtp.PRTP;
import xyz.distemi.prtp.PUtils;
import xyz.distemi.prtp.RoseCost;
import xyz.distemi.prtp.data.Messages;
import xyz.distemi.prtp.data.Profile;
import xyz.distemi.prtp.data.Settings;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PRTPAPI {
    public static void rtpPlayer(Player player, String execute) throws Exception {
        if (!PRTP.profiles.containsKey(execute)) {
            throw new Exception(Messages.noProfile);
        }

        if (execute.startsWith("player:")) {
            Bukkit.dispatchCommand(player, execute.substring(8));
            return;
        }
        if (execute.startsWith("console:")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), execute.substring(9));
            return;
        }
        if (execute.startsWith("message:")) {
            player.sendMessage(PUtils.a(execute.substring(9), player));
            return;
        }
        if (execute.startsWith("none")) {
            return;
        }

        Profile profile = PRTP.profiles.get(execute);

        if (!RoseCost.doCost(profile.cost, player, false, true)) {
            return;
        }

        if (!player.hasPermission("prtp.profile." + profile.name)) {
            throw new Exception(PUtils.a(Messages.noPerm, player));
        }

        boolean center = profile.target.equalsIgnoreCase("center");

        int iter = 0;

        while (iter < Settings.maxTries) {
            int x;
            int z;
            World world;

            if (center) {
                x = PUtils.r(-1 * profile.radius, profile.radius);
                z = PUtils.r(-1 * profile.radius, profile.radius);
                world = Bukkit.getWorld(profile.world);
            } else {
                List<Player> players = Bukkit.getOnlinePlayers().stream()
                        .filter(p -> p.getWorld().getName().equalsIgnoreCase(profile.world) && !p.getName().equals(player.getName()))
                        .collect(Collectors.toList());
                if (players.size() <= 0) {
                    throw new Exception(Messages.worldNoPlayers);
                }
                Player target = players.get(new Random().nextInt(players.size()));
                Location location = target.getLocation();
                x = (int) (location.getX() + PUtils.r(-1 * profile.radius, profile.radius));
                z = (int) (location.getZ() + PUtils.r(-1 * profile.radius, profile.radius));
                world = location.getWorld();
            }

            int y = calcY(world, x, z);
            if (y != -1) {
                if (RoseCost.doCost(profile.cost, player, true, false)) {
                    player.sendMessage(PUtils.a(
                            Messages.teleported
                                    .replaceAll("#X", String.valueOf(x))
                                    .replaceAll("#Y", String.valueOf(y))
                                    .replaceAll("#Z", String.valueOf(z))
                            , player));
                    PUtils.usys(() -> player.teleport(new Location(
                            world,
                            x,
                            y,
                            z
                    )));
                    return;
                }
            }
            iter++;
        }
    }

    public static Collection<Profile> getAllProfiles() {
        return PRTP.profiles.values();
    }

    private static int calcY(World world, int x, int z) {
        PUtils.usysaw(() -> world.getBlockAt(x, 10, z));
        for (int y = 255; y > 0; y--) {
            Block block = world.getBlockAt(x, y, z);
            Material material = block.getType();
            String mat_name = material.name();
            if (Settings.preventBlocks.contains(mat_name)) {
                return -1;
            }
            if (material.isTransparent() || Settings.ignoredBlocks.contains(mat_name)) {
                continue;
            }
            return block.getY() + 1;
        }
        return -1;
    }
}
