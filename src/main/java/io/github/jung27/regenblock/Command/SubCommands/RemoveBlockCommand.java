package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.InvetoryHolder.RemoveBlockHolder;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;


public class RemoveBlockCommand extends SubCommand {
    @Override
    public String getName() {
        return "removeblock";
    }

    @Override
    public String getDescription() {
        return "해당 지역의 출현 블럭을 제거합니다.";
    }

    @Override
    public String getSyntax() {
        return "/regenblock removeblock <id>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length != 2) {
            player.sendMessage("사용법: " + getSyntax());
            return;
        }

        Inventory inv = Bukkit.createInventory(new RemoveBlockHolder(), 54, "블럭 제거: " + args[1]);
        Region region = Region.getRegion(args[1]);
        if(region == null) {
            player.sendMessage("해당 id의 지역이 존재하지 않습니다.");
            return;
        }

        Material[] materials = region.getMaterials();
        for(Material material : materials) {

            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Collections.singletonList("빈도: " + region.getFrequency(material)));
            item.setItemMeta(meta);

            inv.addItem(item);
        }

        player.openInventory(inv);
    }
}
