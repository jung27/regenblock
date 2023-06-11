package io.github.jung27.regenblock.GUI;

import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Inventory.InventoryButton;
import io.github.jung27.regenblock.Inventory.InventoryGUI;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RegionGUI extends InventoryGUI {
    private final Region region;
    public RegionGUI(Region region) {
        this.region = region;
    }
    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9, "지역 편집");
    }

    @Override
    public void decorate(Player player){
        addButton(0, new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.GRASS);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("블럭");
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    player.closeInventory();
                    GUIManager.getInstance().openGUI(new BlockGUI(), player);
                })
        );

        super.decorate(player);
    }
}
