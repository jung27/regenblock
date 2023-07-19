package io.github.jung27.regenblock.GUI;

import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Inventory.InventoryButton;
import io.github.jung27.regenblock.Inventory.InventoryGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RegenBlockGUI extends InventoryGUI {
    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 3*9, "리젠블럭");
    }

    @Override
    public void decorate(Player player)  {
        int inventorySize = this.getInventory().getSize();

        for(int i = 0; i < inventorySize; i++) {
            int color = i % 2 == 0? 7 : 5;
            ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) color);
            this.getInventory().setItem(i, itemStack);
        }

        InventoryButton listBtn = new InventoryButton()
                .creator(p -> {
                    ItemStack stack = new ItemStack(Material.BOOK);
                    ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "리젠지역 목록");
                    stack.setItemMeta(meta);
                    return stack;
                })
                .consumer(event -> GUIManager.getInstance().openGUI(new RegionListGUI(), player));

        InventoryButton globalSetting = new InventoryButton()
                .creator(p -> {
                    ItemStack stack = new ItemStack(Material.NETHER_STAR);
                    ItemMeta meta = stack.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "전역 설정");
                    stack.setItemMeta(meta);
                    return stack;
                })
                .consumer(event -> GUIManager.getInstance().openGUI(new GlobalSettingGUI(), player));

        addButton(10, listBtn);
        addButton(12, globalSetting);

        super.decorate(player);
    }
}
