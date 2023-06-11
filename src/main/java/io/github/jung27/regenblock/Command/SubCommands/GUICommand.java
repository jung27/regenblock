package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.GUI.RegenBlockGUI;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.InvetoryHolder.BlockHolder;
import io.github.jung27.regenblock.RegenBlock;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

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
