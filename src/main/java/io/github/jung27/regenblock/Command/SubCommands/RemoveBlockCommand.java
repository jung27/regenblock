package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        Inventory inv = Bukkit.createInventory(null, 54, "블럭 제거");
        Region region = Region.getRegion(args[1]);
        if(region == null) {
            player.sendMessage("해당 id의 지역이 존재하지 않습니다.");
            return;
        }

        Material[] materials = region.getMaterials();
        for(Material material : materials) {
            inv.addItem(new ItemStack(material));
        }

        player.openInventory(inv);
    }
}
