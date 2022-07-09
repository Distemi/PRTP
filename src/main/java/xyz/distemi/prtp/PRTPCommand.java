package xyz.distemi.prtp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.distemi.prtp.api.PRTPAPI;
import xyz.distemi.prtp.data.Messages;
import xyz.distemi.prtp.data.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PRTPCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String @NotNull [] args) {
        PUtils.uasy(() -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Players only!");
                return;
            }

            String execute = Settings.defaultCommand;

            if (args.length > 0) {
                execute = args[0];
                if (execute.equalsIgnoreCase("reload") && sender.hasPermission("prtp.reload")) {
                    PRTP.THIS.reloadConfig();
                    PRTP.THIS.parseConfig();
                    sender.sendMessage(Messages.configReloaded);
                    return;
                }
                if (execute.contains(":")) {
                    sender.sendMessage(Messages.noProfile);
                    return;
                }
            }

            try {
                PRTPAPI.rtpPlayer(((Player) sender), execute);
            } catch (Exception exception) {
                sender.sendMessage(exception.getMessage());
            }

            /*String execute = Settings.defaultCommand;

            if (!PRTP.profiles.containsKey(execute)) {
                sender.sendMessage(Messages.noProfile);
                return;
            }

            Player player = (Player) sender;

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
                sender.sendMessage(PUtils.a(Messages.noPerm, player));
                return;
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
                        player.sendMessage(Messages.worldNoPlayers);
                        return;
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
            }*/

        });
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String @NotNull [] args) {
        if (args.length >= 2) {
            return new ArrayList<>();
        }
        List<String> list = PRTP.profiles
                .keySet().stream()
                .filter(name -> sender.hasPermission("prtp.profile." + name))
                .collect(Collectors.toList());
        if (sender.hasPermission("prtp.reload")) {
            list.add("reload");
        }
        return list;
    }

}
