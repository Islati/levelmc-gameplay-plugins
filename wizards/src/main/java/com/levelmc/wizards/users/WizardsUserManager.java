package com.levelmc.wizards.users;

import com.levelmc.core.api.players.UserManager;
import com.levelmc.core.api.yml.InvalidConfigurationException;
import com.levelmc.wizards.Wizards;
import org.bukkit.entity.Player;

import java.io.File;

public class WizardsUserManager extends UserManager<Wizard> {
    private static final String USER_DATA_FOLDER = "users/";
    private static final String USER_DATA_FILE_NAME_FORMAT = "users/%s.yml";

    public WizardsUserManager() {
        super(Wizards.getInstance(), Wizard.class);

        File dataFolder = new File(USER_DATA_FOLDER);
        dataFolder.mkdirs();
    }

    @Override
    public void addUser(Player p) {
        super.addUser(p);

        Wizard user = getUser(p);

        File userFile = new File(getParent().getDataFolder(), String.format(USER_DATA_FILE_NAME_FORMAT, user.getId().toString()));
        try {
            user.init(userFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean removeUser(Player p) {
        Wizard user = getUser(p);
        super.removeUser(p);

        File userFile = new File(getParent().getDataFolder(), String.format(USER_DATA_FILE_NAME_FORMAT, user.getId().toString()));
        try {
            user.save(userFile);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
