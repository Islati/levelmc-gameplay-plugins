package com.levelmc.skills.command;

import com.levelmc.core.chat.HelpScreen;
import com.levelmc.core.cmd.Command;
import com.levelmc.core.cmd.commands.CommandInfo;
import com.levelmc.skills.Skills;
import com.levelmc.skills.menus.SkillsOverviewMenu;
import org.bukkit.entity.Player;

@CommandInfo(name = "skills", aliases = {"sk"}, description = "View & Manage your skills", usage = "/sk")
public class SkillsCommand {

    private HelpScreen skillsHelp = new HelpScreen("Skills Command Help")
            .addEntry("skills","Opens the skill menu");

    public SkillsCommand() {

    }

    @Command(identifier = "skills",description = "Open skills overview menu")
    public void onSkillsCommand(Player player) {
        new SkillsOverviewMenu(Skills.getInstance().getUserManager().getUser(player)).openMenu(player);
    }
}
