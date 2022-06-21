package xyz.distemi.prtp;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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

        if ((player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE))) {
            if (cost.startsWith("food:")) {
                int food = Integer.parseUnsignedInt(cost.split(":")[1]);
                if (player.getFoodLevel() <= food) {
                    if (notify) player.sendMessage(PUtils.a(
                            Messages.costsNoFood.replaceAll("#Val", String.valueOf(food)), player));
                    return false;
                }
                if (take) {
                    player.setFoodLevel(player.getFoodLevel() - food);
                }
                return true;
            }

            if (cost.startsWith("health:")) {
                int health = Integer.parseUnsignedInt(cost.split(":")[1]);
                if (player.getHealth() - health <= 1) {
                    if (notify) player.sendMessage(PUtils.a(
                            Messages.costsNoHealth.replaceAll("#Val", String.valueOf(health)), player));
                    return false;
                }
                if (take) {
                    player.damage(health);
                }
                return true;
            }
        }

        if (economy != null) {
            if (cost.startsWith("eco:")) {
                int eco = Integer.parseUnsignedInt(cost.split(":")[1]);
                if (!economy.has(player, eco)) {
                    if (notify) player.sendMessage(PUtils.a(
                            Messages.costsNoEco.replaceAll("#Val", String.valueOf(eco)), player));
                    return false;
                }
                if (take) {
                    economy.withdrawPlayer(player, eco);
                }
                return true;
            }
        }

        return true;
    }
}
