package io.github.jung27.regenblock.GUI;

import io.github.jung27.regenblock.Inventory.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BlockGUI extends InventoryGUI {

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9, "블럭");
    }

    @Override
    public void decorate(Player player){
        super.decorate(player);
    }
}
