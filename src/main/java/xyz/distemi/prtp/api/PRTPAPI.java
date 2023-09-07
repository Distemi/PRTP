package xyz.distemi.prtp.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import xyz.distemi.prtp.PRTP;
import xyz.distemi.prtp.PUtils;
import xyz.distemi.prtp.RoseCost;
import xyz.distemi.prtp.data.Messages;
import xyz.distemi.prtp.data.Profile;
import xyz.distemi.prtp.data.Settings;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PRTPAPI {
    public static void rtpPlayer(Player player, String execute, Consumer<Exception> errorHandler) {
        if (!PRTP.profiles.containsKey(execute)) {
            errorHandler.accept(new Exception(Messages.noProfile));
            return;
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
            errorHandler.accept(new Exception(PUtils.a(Messages.noPerm, player)));
            return;
        }

        boolean center = profile.target.equalsIgnoreCase("center");

        long start = System.currentTimeMillis();
        tryTeleports(0, Settings.maxTries, profile, player, center, errorHandler, start);
    }

    private static void tryTeleports(int current, int max, Profile profile, Player player, boolean center, Consumer<Exception> errorHandler, long startTime) {
        if (current >= max) {
            errorHandler.accept(new Exception(Messages.failedToFindAPlace));
            return;
        }
        int x;
        int z;
        World world;

        if (center) {
            x = PUtils.r(-1 * profile.radius, profile.radius);
            z = PUtils.r(-1 * profile.radius, profile.radius);

            double distance = Math.sqrt(x * x + z * z);
            while (distance < profile.minimalRadius) {
                x = PUtils.r(-1 * profile.radius, profile.radius);
                z = PUtils.r(-1 * profile.radius, profile.radius);
                distance = Math.sqrt(x * x + z * z);
            }

            world = Bukkit.getWorld(profile.world);
        } else {
            List<Player> players = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getWorld().getName().equalsIgnoreCase(profile.world) && !p.getName().equals(player.getName()))
                    .collect(Collectors.toList());
            if (players.isEmpty()) {
                errorHandler.accept(new Exception(Messages.worldNoPlayers));
                return;
            }
            Player target = players.get(ThreadLocalRandom.current().nextInt(players.size()));
            Location location = target.getLocation();

            x = location.getBlockX() + PUtils.r(-1 * profile.radius, profile.radius);
            z = location.getBlockZ() + PUtils.r(-1 * profile.radius, profile.radius);

            double distance = Math.sqrt(x * x + z * z);
            while (distance < profile.minimalRadius) {
                x = (int) (location.getX() + PUtils.r(-1 * profile.radius, profile.radius));
                z = (int) (location.getZ() + PUtils.r(-1 * profile.radius, profile.radius));
                distance = Math.sqrt(x * x + z * z);
            }


            world = location.getWorld();
        }

        final boolean[] completed = {false};

        final int finalX = x;
        final int finalZ = z;

        PUtils.calcY(world, x, z, y -> {
            if (completed[0]) {
                return;
            }
            completed[0] = true;
            if (y == -99999) {
                tryTeleports(current + 1, max, profile, player, center, errorHandler, startTime);
                return;
            }
            if (RoseCost.doCost(profile.cost, player, true, false)) {
                long endTime = System.currentTimeMillis();
                player.sendMessage(PUtils.a(
                        Messages.teleported
                                .replaceAll("#X", String.valueOf(finalX))
                                .replaceAll("#Y", String.valueOf(y))
                                .replaceAll("#Z", String.valueOf(finalZ))
                                .replaceAll("#T", String.valueOf(endTime - startTime))
                        , player));
                Bukkit.getScheduler().runTask(PRTP.THIS, () -> profile.animation.process(player, new Location(
                        world,
                        finalX,
                        y,
                        finalZ
                )));
            }
        });
    }

    public static Collection<Profile> getAllProfiles() {
        return PRTP.profiles.values();
    }

}
