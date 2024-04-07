package io.github.jung27.regenblock.Event;

import io.github.jung27.regenblock.Appointor.Appointor;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Region.Region;
import io.github.jung27.regenblock.Setting.GlobalSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class RegenBlockEventListener implements Listener {
    final GUIManager guiManager = GUIManager.getInstance();
    final GlobalSetting globalSetting = GlobalSetting.getInstance();
    private final HashMap<UUID, Appointor> appointors = new HashMap<>();
    public void addAppointor(UUID uuid, Appointor appointor){
        appointors.put(uuid, appointor);
    }
    public void removeAppointor(UUID uuid){
        appointors.remove(uuid);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        guiManager.handleClick(event);
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event){
        guiManager.handleOpen(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event){
        guiManager.handleClose(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location blockLocation = event.getBlock().getLocation();

        Appointor appointor = appointors.get(player.getUniqueId());

        //좌표 지정
        if(appointor != null) {
            event.setCancelled(true);
            appointor.appointFirst(blockLocation);
        }

        //블럭 리젠
        Region region = Region.regenBlock(blockLocation);
        if(region != null) {
            if(!region.isExpDrop()){
                event.setExpToDrop(0);
            }
            if(globalSetting.isAutoPickup()){
                event.setDropItems(false);
                Collection<ItemStack> drops = event.getBlock().getDrops(player.getInventory().getItemInMainHand());
                player.getInventory().addItem(drops.toArray(new ItemStack[0]));
            }
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(Region.getRegionByLocation(event.getBlock().getLocation()) != null) {
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().forEach(block -> Region.regenBlock(block.getLocation()));
    }
    @EventHandler
    public void  onDecay(LeavesDecayEvent event) {
        Region.regenBlock(event.getBlock().getLocation());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Appointor appointor = appointors.get(player.getUniqueId());

        if(appointor == null || !event.hasBlock() ||
                event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_AIR
        ) return;

        Location location = event.getClickedBlock().getLocation();

        event.setCancelled(true);
        appointor.appointSecond(location);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Region.regenBlock(event.getBlock().getLocation());
    }
}