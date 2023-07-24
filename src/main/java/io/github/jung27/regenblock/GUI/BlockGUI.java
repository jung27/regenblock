package io.github.jung27.regenblock.GUI;

import io.github.jung27.regenblock.Conversation.FrequencyPrompt;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Inventory.InventoryButton;
import io.github.jung27.regenblock.Inventory.InventoryGUI;
import io.github.jung27.regenblock.RegenBlock;
import io.github.jung27.regenblock.Region.RegenMaterial;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collections;

public class BlockGUI extends InventoryGUI {
    private final Region region;
    private int currentPage = 0;
    public BlockGUI(Region region) {
        this.region = region;
    }

    @Override
    protected Inventory createInventory() {
        return Bukkit.createInventory(null, 54, "블럭");
    }

    @Override
    public void decorate(Player player){
        int inventorySize = this.getInventory().getSize();
        RegenMaterial[] materials = region.getMaterials();

        for(int i = 0; i < inventorySize-9; i++) {
            int index = currentPage*(inventorySize-9)+i;
            if(index >= materials.length) break;
            RegenMaterial m = materials[index];
            addButton(i, new InventoryButton()
                    .creator(p -> {
                        ItemStack item = new ItemStack(m.getMaterial(), 1, m.getData());
                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setLore(Collections.singletonList(ChatColor.YELLOW + "빈도: " + region.getFrequency(m)));
                        item.setItemMeta(itemMeta);
                        return item;
                    })
                    .consumer(event -> {
                        switch (event.getClick()) {
                            case LEFT:
                                region.removeBlock(m);
                                getInventory().clear();
                                reload(player);
                                break;
                            case RIGHT:
                                //이 부분은 채팅으로 설정
                                ConversationFactory cf = new ConversationFactory(RegenBlock.instance());
                                Conversation conv = cf
                                        .withFirstPrompt(new FrequencyPrompt(region, m))
                                        .withLocalEcho(false)
                                        .buildConversation((Player) event.getWhoClicked());
                                conv.begin();
                                player.closeInventory();
                            default:
                        }
                    })
            );
        }

        if(currentPage > 0) addButton(inventorySize-9, new InventoryButton()
                .creator(p -> new ItemStack(Material.ARROW))
                .consumer(event -> {
                    currentPage--;
                    reload(player);
                }));

        addButton(inventorySize-5, new InventoryButton()
                .creator(p -> {
                    ItemStack item = new ItemStack(Material.BARRIER);
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(ChatColor.RED + "닫기");
                    item.setItemMeta(itemMeta);
                    return item;
                })
                .consumer(event -> {
                    player.closeInventory();
                    GUIManager.getInstance().openGUI(new RegionGUI(region), player);
                }));

        System.out.println((currentPage+1)*(inventorySize-9) + "," + region.getFrequencies().size());
        if((currentPage+1)*(inventorySize-9) <= region.getFrequencies().size()) addButton(inventorySize-1, new InventoryButton()
                .creator(p -> new ItemStack(Material.ARROW))
                .consumer(event -> {
                    currentPage++;
                    reload(player);
                }));

        super.decorate(player);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        super.onClick(event);
        InventoryView view = event.getView();
        ItemStack item = event.getCurrentItem();
        RegenMaterial material = RegenMaterial.get(item.getType(), item.getData().getData());
        if (event.getClickedInventory() == view.getBottomInventory()) {
            region.addBlock(material, 1);
            reload((Player) event.getWhoClicked());
        }
    }
}
