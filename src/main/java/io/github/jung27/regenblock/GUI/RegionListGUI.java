package io.github.jung27.regenblock.GUI;

import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Inventory.InventoryButton;
import io.github.jung27.regenblock.Inventory.InventoryGUI;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RegionListGUI extends InventoryGUI {
    private int currentPage = 0;

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 6*9, "리젠 지역");
    }

    @Override
    public void decorate(Player player) {
        int inventorySize = this.getInventory().getSize();

        for(int i = 0; i < inventorySize-9; i++) {
            int index = currentPage*(inventorySize-9)+i;
            if(index >= Region.regions.size()) break;
            Region region = Region.regions.get(index);
            addButton(i, new InventoryButton()
                    .creator(p -> {
                        ItemStack item = new ItemStack(Material.PAPER);
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.WHITE+ region.getId());
                        item.setItemMeta(itemMeta);
                        return item;
                    })
                    .consumer(event -> GUIManager.getInstance().openGUI(new RegionGUI(region), player))
            );
        }

        if(currentPage > 0) addButton(inventorySize-9, new InventoryButton()
                .creator(p -> new ItemStack(Material.ARROW))
                .consumer(event -> {
                    currentPage--;
                    reload(player);
                }));

        if((currentPage+1)*(inventorySize-9) < Region.regions.size()) addButton(inventorySize-1, new InventoryButton()
                .creator(p -> new ItemStack(Material.ARROW))
                .consumer(event -> {
                    currentPage++;
                    reload(player);
                }));


        super.decorate(player);
    }
}
