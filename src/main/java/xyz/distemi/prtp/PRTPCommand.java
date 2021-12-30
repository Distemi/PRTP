package xyz.distemi.prtp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.distemi.prtp.data.Messages;
import xyz.distemi.prtp.data.Profile;
import xyz.distemi.prtp.data.Settings;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PRTPCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String @NotNull [] args) {
        String execute = Settings.defaultCommand;
        if (args.length > 0) {
            execute = args[0];
            if (execute.equalsIgnoreCase("reload") && sender.hasPermission("prtp.reload")) {
                PRTP.THIS.reloadConfig();
                PRTP.THIS.parseConfig();
                sender.sendMessage(ChatColor.GREEN + "Reloaded!");
                return true;
            }
            if (execute.contains(":")) {
                sender.sendMessage(Messages.noProfile);
                return true;
            }
        }

        if (!PRTP.profiles.containsKey(execute)) {
            sender.sendMessage(Messages.noProfile);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Players only!");
            return true;
        }
        Player player = (Player) sender;

        if (execute.startsWith("player:")) {
            Bukkit.dispatchCommand(player, execute.substring(8));
            return true;
        }
        if (execute.startsWith("console:")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), execute.substring(9));
            return true;
        }
        if (execute.startsWith("message:")) {
            player.sendMessage(PUtils.a(execute.substring(9), player));
            return true;
        }
        if (execute.startsWith("none")) {
            return true;
        }

        Profile profile = PRTP.profiles.get(execute);

        if (!RoseCost.doCost(profile.cost, player, false, true)) {
            return true;
        }

        if (!player.hasPermission("prtp.profile." + profile.name)) {
            sender.sendMessage(PUtils.a(Messages.noPerm, player));
            return true;
        }

        boolean center = profile.target.equalsIgnoreCase("center");
        if (center) {
            int x = PUtils.r(-1 * profile.radius, profile.radius);
            int z = PUtils.r(-1 * profile.radius, profile.radius);
            World world = Bukkit.getWorld(profile.world);
            int y = calcY(world, x, z);
            player.teleport(new Location(
                    world,
                    x,
                    y,
                    z
            ));
            if (RoseCost.doCost(profile.cost, player, true, false)) {
                player.sendMessage(PUtils.a(
                        Messages.teleported
                                .replaceAll("#X", String.valueOf(x))
                                .replaceAll("#Y", String.valueOf(y))
                                .replaceAll("#Z", String.valueOf(z))
                        , player));
            }
        } else {
            List<Player> players = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getWorld().getName().equalsIgnoreCase(profile.world) && !p.getName().equals(player.getName()))
                    .collect(Collectors.toList());
            if (players.size() <= 0) {
                player.sendMessage(Messages.worldNoPlayers);
                return true;
            }
            Player target = players.get(new Random().nextInt(players.size()));
            Location location = target.getLocation();
            int x = (int) (location.getX() + PUtils.r(-1 * profile.radius, profile.radius));
            int z = (int) (location.getZ() + PUtils.r(-1 * profile.radius, profile.radius));
            World world = location.getWorld();
            int y = calcY(world, x, z);
            player.teleport(new Location(
                    world,
                    x,
                    y,
                    z
            ));
            if (RoseCost.doCost(profile.cost, player, true, false)) {
                player.sendMessage(PUtils.a(
                        Messages.teleported
                                .replaceAll("#X", String.valueOf(x))
                                .replaceAll("#Y", String.valueOf(y))
                                .replaceAll("#Z", String.valueOf(z))
                        , player));
            }
        }
        return true;
    }

    private int calcY(World world, int x, int z) {
        for (int y = 255; y > 0; y--) {
            Block block = world.getBlockAt(x, y, z);
            if (Settings.ignoredBlocks.contains(block.getType().toString())) {
                continue;
            }
            return block.getY() + 1;
        }
        return 115;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String @NotNull [] args) {
        return args.length >= 2 ? Collections.emptyList() :
                PRTP.profiles
                        .keySet().stream()
                        .filter(name -> sender.hasPermission("prtp.profile." + name))
                        .collect(Collectors.toList());
    }

}
