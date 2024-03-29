package io.github.jung27.regenblock.GUI;

import com.cryptomorin.xseries.XMaterial;
import io.github.jung27.regenblock.Conversation.RegenDelayPrompt;
import io.github.jung27.regenblock.Conversation.IdPrompt;
import io.github.jung27.regenblock.Appointor.ModifyAppointor;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Inventory.InventoryButton;
import io.github.jung27.regenblock.Inventory.InventoryGUI;
import io.github.jung27.regenblock.RegenBlock;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class RegionGUI extends InventoryGUI {
    private final Region region;
    public RegionGUI(Region region) {
        this.region = region;
    }
    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 9, "지역 편집: " + region.getId());
    }

    @Override
    public void decorate(Player player){
        addButton(0, new InventoryButton()
                .creator(p -> {
                    ItemStack item = XMaterial.GRASS_BLOCK.parseItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "블럭");
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    player.closeInventory();
                    GUIManager.getInstance().openGUI(new BlockGUI(region), player);
                })
        );
        addButton(1, new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.LAVA_BUCKET);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "삭제");
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    Region.regions.remove(region);
                    player.closeInventory();
                    GUIManager.getInstance().openGUI(new RegionListGUI(), player);
                })
        );
        addButton(2, new InventoryButton()
                .creator(p -> {
                    ItemStack item = XMaterial.TOTEM_OF_UNDYING.parseItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "복제");
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    region.clone();
                    player.sendMessage(ChatColor.GREEN + "복제되었습니다.");
                })
        );
        addButton(3, new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.NAME_TAG);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "id 변경");
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    ConversationFactory cf = new ConversationFactory(RegenBlock.instance());
                    Conversation conv = cf
                            .withFirstPrompt(new IdPrompt(region))
                            .withLocalEcho(false)
                            .buildConversation((Player) event.getWhoClicked());
                    conv.begin();
                    player.closeInventory();
                })
        );
        addButton(4, new InventoryButton()
                .creator(p -> {
                    ItemStack item = XMaterial.CLOCK.parseItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "리젠 딜레이 변경");
                    meta.setLore(Collections.singletonList(ChatColor.YELLOW + "현재 리젠 딜레이: " + region.getRegenDelay()));
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    ConversationFactory cf = new ConversationFactory(RegenBlock.instance());
                    Conversation conv = cf
                            .withFirstPrompt(new RegenDelayPrompt(region))
                            .withLocalEcho(false)
                            .buildConversation((Player) event.getWhoClicked());
                    conv.begin();
                    player.closeInventory();
                })
        );
        addButton(5, new InventoryButton()
                .creator(p -> {
                    ItemStack item = XMaterial.MAP.parseItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "구역 재설정");
                    meta.setLore(Arrays.asList(ChatColor.YELLOW + "시작 위치: " + l2s(region.getStartLocation()), ChatColor.YELLOW + "끝 위치: " + l2s(region.getEndLocation())));
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    player.sendMessage("좌클릭과 우클릭으로 두 지점을 지정해주세요.");

                    ModifyAppointor appointor = new ModifyAppointor(player, region.getId());
                    appointor.run();
                    player.closeInventory();
                })
        );
        addButton(6, new InventoryButton()
                .creator(p -> {
                    ItemStack item = XMaterial.EXPERIENCE_BOTTLE.parseItem();
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.WHITE + "레벨 지급");
                    meta.setLore(Collections.singletonList(ChatColor.YELLOW + ((region.isExpDrop()) ? "ON" : "OFF")));
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    region.setExpDrop(!region.isExpDrop());
                    player.sendMessage(ChatColor.GREEN + "레벨 지급이 " + ((region.isExpDrop()) ? "ON" : "OFF") + "으로 변경되었습니다.");
                    reload(player);
                })
        );
        addButton(8, new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.BARRIER);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RED + "닫기");
                    item.setItemMeta(meta);
                    return item;
                })
                .consumer(event -> {
                    player.closeInventory();
                    GUIManager.getInstance().openGUI(new RegionListGUI(), player);
                })
        );

        super.decorate(player);
    }

    String l2s(Location location) {
        return "("+location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()+")";
    }
}
