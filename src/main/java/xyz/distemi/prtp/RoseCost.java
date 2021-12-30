package xyz.distemi.prtp;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.distemi.prtp.data.Messages;

public class RoseCost {
    private static Economy economy;

    public static boolean setEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public static boolean doCost(String cost, Player player, boolean take, boolean notify) {

        if (cost.startsWith("none")) {
            return true;
        }

        if (cost.startsWith("food:")) {
            int food = Integer.parseUnsignedInt(cost.substring(6));
            if (player.getFoodLevel() >= food) {
                if (notify) player.sendMessage(PUtils.a(Messages.costsNoFood, player));
                return false;
            }
            if (take) {
                player.setFoodLevel(player.getFoodLevel() - food);
            }
            return true;
        }

        if (cost.startsWith("eco:")) {
            int eco = Integer.parseUnsignedInt(cost.substring(5));
            if (!economy.has(player, eco)) {
                if (notify) player.sendMessage(PUtils.a(Messages.costsNoEco, player));
                return false;
            }
            if (take) {
                economy.withdrawPlayer(player, eco);
            }
            return true;
        }

        if (cost.startsWith("health:")) {
            int health = Integer.parseUnsignedInt(cost.substring(8));
            if (player.getHealth() - health <= 1) {
                if (notify) player.sendMessage(PUtils.a(Messages.costsNoHealth, player));
                return false;
            }
            if (take) {
                player.damage(health);
            }
            return true;
        }

        return true;
    }
}