package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.GUI.RegenBlockGUI;
import io.github.jung27.regenblock.Inventory.GUIManager;
import org.bukkit.entity.Player;

public class GUICommand extends SubCommand {
    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public String getDescription() {
        return "리젠블럭의 기능을 관리합니다.";
    }

    @Override
    public String getSyntax() {
        return "/regenblock gui";
    }

    @Override
    public void perform(Player player, String[] args) {
        GUIManager.getInstance().openGUI(new RegenBlockGUI(), player);
    }
}
