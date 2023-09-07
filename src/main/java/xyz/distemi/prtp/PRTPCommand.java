package xyz.distemi.prtp;

import org.bukkit.Bukkit;
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
        Bukkit.getScheduler().runTaskAsynchronously(PRTP.THIS, () -> {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "For players only!");
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


            PRTPAPI.rtpPlayer(((Player) sender), execute, error -> sender.sendMessage(error.getMessage()));

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
