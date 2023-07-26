package io.github.jung27.regenblock.GUI;

import com.cryptomorin.xseries.XMaterial;
import io.github.jung27.regenblock.Inventory.InventoryButton;
import io.github.jung27.regenblock.Inventory.InventoryGUI;
import io.github.jung27.regenblock.Setting.GlobalSetting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class GlobalSettingGUI extends InventoryGUI {
    private final GlobalSetting globalSetting = GlobalSetting.getInstance();
    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9, "전역 설정");
    }

    @Override
    public void decorate(Player player) {

        addButton(0, new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.WATER_BUCKET);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "자동 채우기: " + (globalSetting.isAutoFill()? "ON" : "OFF"));
                    meta.setLore(Collections.singletonList(ChatColor.YELLOW + "범위 지정시 자동으로 블럭을 채웁니다."));
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    globalSetting.setAutoFill(!globalSetting.isAutoFill());
                    reload(player);
                })

                .creator(p -> {
                    ItemStack item = XMaterial.LEAD.parseItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "자동 줍기: " + (globalSetting.isAutoFill()? "ON" : "OFF"));
                    meta.setLore(Collections.singletonList(ChatColor.YELLOW + "채굴 시 드롭 아이템이 자동으로 인벤토리에 지급됩니다."));
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    globalSetting.setAutoPickup(!globalSetting.isAutoPickup());
                    reload(player);
                })
        );

        super.decorate(player);
    }
}
