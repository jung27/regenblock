package io.github.jung27.regenblock.Event;

import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Region.Region;
import javafx.util.Pair;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.UUID;

public class RegenBlockEventListener implements Listener {
    private final HashMap<UUID, String> AppointingPlayers = new HashMap<>();
    private final HashMap<UUID, Pair<String, Material>> playersSettingFre = new HashMap<>();
    private final HashMap<UUID, Location[]> AppointingLocations = new HashMap<>();
    public void addAppointingPlayer(Player player, String id) {
        AppointingPlayers.put(player.getUniqueId(), id);
    }
    public void removeAppointingPlayer(Player player) {
        AppointingPlayers.remove(player.getUniqueId());
    }
    public final GUIManager guiManager = GUIManager.getInstance();

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
        Location blockLocation = event.getBlock().getLocation();

        //좌표 지정
        if(AppointingPlayers.containsKey(event.getPlayer().getUniqueId())) {

            event.setCancelled(true);
            appointLocation(event.getPlayer(), blockLocation, 0);

        }

        //블럭 리젠
        Region.regenBlock(blockLocation);
    }

    @EventHandler
    public void  onBlockExplode(BlockExplodeEvent event) {
        event.blockList().forEach(block -> Region.regenBlock(block.getLocation()));
    }
    @EventHandler
    public void  onDecay(LeavesDecayEvent event) {
        Region.regenBlock(event.getBlock().getLocation());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!AppointingPlayers.containsKey(event.getPlayer().getUniqueId()) || !event.hasBlock() ||
                event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_AIR
        ) return;

        event.setCancelled(true);
        appointLocation(event.getPlayer(), event.getClickedBlock().getLocation(), 1);
    }

    //좌표 지정 함수
    private void appointLocation(Player player, Location location, int index) {
        UUID uuid = player.getUniqueId();

        try{
            if(AppointingLocations.containsKey(uuid)){
                AppointingLocations.get(uuid)[index] = location;
            } else{
                Location[] locations = new Location[2];
                locations[index] = location;
                System.out.println(locations[0] + " " + locations[1]);
                AppointingLocations.put(uuid, locations);
            }
        } catch (ArrayIndexOutOfBoundsException e){
            player.sendMessage("좌표 지정에 실패했습니다.");
            removeAppointingPlayer(player);
            e.printStackTrace();
        }

        if (AppointingLocations.get(uuid)[0] != null && AppointingLocations.get(uuid)[1] != null) {
            player.sendMessage("좌표가 정상적으로 지정되었습니다.");
            createRegion(player, AppointingPlayers.get(uuid));
            removeAppointingPlayer(player);
            AppointingLocations.remove(uuid);
        }
    }

    private void createRegion(Player player, String id) {
        Location[] locations = AppointingLocations.get(player.getUniqueId());
        new Region(locations[0], locations[1], id);
    }
}