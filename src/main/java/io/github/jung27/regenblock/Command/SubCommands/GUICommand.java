package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
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
        Inventory inv = Bukkit.createInventory(new BlockHolder(), 54, "리젠블럭");

        for(Region region : Region.regions) {

            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(region.getId());
            item.setItemMeta(meta);

            inv.addItem(item);
        }

        player.openInventory(inv);
    }
}
