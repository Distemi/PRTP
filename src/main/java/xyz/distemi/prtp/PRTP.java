package xyz.distemi.prtp;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.distemi.prtp.data.Messages;
import xyz.distemi.prtp.data.Profile;
import xyz.distemi.prtp.data.Settings;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public final class PRTP extends JavaPlugin {
    public static HashMap<String, Profile> profiles = new HashMap<>();

    public static Logger logger;

    public static PRTP THIS;

    public void parseConfig() {
        FileConfiguration cfg = getConfig();


        ConfigurationSection mess_section = cfg.getConfigurationSection("messages");

        Messages.noPerm = PUtils.b(mess_section.getString("noPerm", "&fSorry, you're don't has permission for this action&8(&7$Perm&8)."));
        Messages.teleported = PUtils.b(mess_section.getString("teleported", "&fYoy are teleported to X: $X Y: $Y Z: $Z."));
        Messages.worldNoPlayers = PUtils.b(mess_section.getString("worldNoPlayers", "&fSorry, teleportation don't can be executed when player's not found."));
        Messages.noProfile = PUtils.b(mess_section.getString("noProfile", "&cTeleportation profile not found!"));

        Messages.costsNoFood = PUtils.b(mess_section.getString("costs.noFood", "&cSorry, you're don't has $Val food to teleportation."));
        Messages.costsNoEco = PUtils.b(mess_section.getString("costs.noEco", "&cSorry, you're don't has $Val dollars to teleportation."));
        Messages.costsNoHealth = PUtils.b(mess_section.getString("costs.noHealth", "&cSorry, you're don't has $Val health to teleportation."));


        ConfigurationSection sett_section = cfg.getConfigurationSection("settings");
        Settings.defaultCommand = sett_section.getString("default-command", "random");
        Settings.ignoredBlocks = sett_section.getStringList("ignored-blocks");


        profiles.clear();
        for (Map<?, ?> val : cfg.getMapList("profiles")) {
            if (val.containsKey("name") && val.containsKey("radius")
                    && val.containsKey("world") && val.containsKey("regarding")
            ) {
                String cost = (String) val.get("cost");
                if (Objects.requireNonNull(cost).length() <= 2 || !val.containsKey("cost")) {
                    cost = "none";
                }
                Profile profile = new Profile();
                profile.name = (String) val.get("name");
                profile.radius = Integer.parseInt((String) val.get("radius"));
                profile.world = (String) val.get("world");
                profile.target = (String) val.get("regarding");
                profile.cost = cost;
                profiles.put(profile.name, profile);
                logger.fine(String.format("Зарегестрирован профиль телепортации %s", profile.name));
            }
        }
    }

    @Override
    public void onEnable() {
        logger = getLogger();
        THIS = this;

        logger.fine("Initializing Prototype RTP...");

        try {
            saveDefaultConfig();
            parseConfig();

            if (!RoseCost.setEconomy()) {
                throw new Exception("Failed initialize economy from Vault");
            }

            PluginCommand command = getCommand("prtp");
            PRTPCommand prtpCommand = new PRTPCommand();
            command.setExecutor(prtpCommand);
            command.setTabCompleter(prtpCommand);
        } catch (Exception exception) {
            logger.severe("Error while initializing plugin: ");
            exception.printStackTrace();
            return;
        }

        logger.fine("Initialized successful!");
    }

    @Override
    public void onDisable() {

    }
}
