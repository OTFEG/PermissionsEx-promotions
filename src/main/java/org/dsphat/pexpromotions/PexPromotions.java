package org.dsphat.pexpromotions;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * PexPromotions - a promotions plugin that doesn't need PermissionsEx.
 * @author DSPhat - 2014
 */
public class PexPromotions extends JavaPlugin {

    public static Permission perm = null;

    @Override
    public void onEnable() {
        if (!setupPermissions()) {
            getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permProvider != null) {
            perm = permProvider.getProvider();
        }
        return (perm != null);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player plyr = (Player)sender;
        String[] plyrGroups = perm.getPlayerGroups(plyr);
        for (String group : plyrGroups) {
            if (group.equalsIgnoreCase(getConfig().getString("from_group"))) {
                return promotePlayer(plyr);
            }
        }
        return false;
    }

    private boolean promotePlayer(Player plyr) {
        perm.playerRemoveGroup(plyr, getConfig().getString("from_group"));
        perm.playerAddGroup(plyr, getConfig().getString("to_group"));
        return true;
    }
}