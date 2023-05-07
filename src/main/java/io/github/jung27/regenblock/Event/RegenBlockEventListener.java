package io.github.jung27.regenblock.Event;

import io.github.jung27.regenblock.InvetoryHolder.RemoveBlockHolder;
import io.github.jung27.regenblock.RegenBlock;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class RegenBlockEventListener implements Listener {
    private final HashMap<UUID, String> AppointingPlayers = new HashMap<>();
    private final HashMap<UUID, Location[]> AppointingLocations = new HashMap<>();

    public void addAppointingPlayer(Player player, String id) {
        AppointingPlayers.put(player.getUniqueId(), id);
    }

    public void removeAppointingPlayer(Player player) {
        AppointingPlayers.remove(player.getUniqueId());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location blockLocation = event.getBlock().getLocation();

        //좌표 지정
        if(AppointingPlayers.containsKey(event.getPlayer().getUniqueId())) {

            event.setCancelled(true);
            AppointLocation(event.getPlayer(), blockLocation, 0);

        }

        //블럭 리젠
        for (Region region : Region.regions) {
            if (region.isInside(blockLocation)) {
                Bukkit.getScheduler().runTaskLater(RegenBlock.instance(), () -> blockLocation.getBlock().setType(region.getMaterial()), 40);
                return;
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(!AppointingPlayers.containsKey(event.getPlayer().getUniqueId()) || !event.hasBlock() ||
                event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR ||
                event.getAction() == Action.RIGHT_CLICK_AIR
        ) return;

        event.setCancelled(true);
        AppointLocation(event.getPlayer(), event.getClickedBlock().getLocation(), 1);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if(inventory.getHolder() instanceof RemoveBlockHolder){
            String id = inventory.getTitle().replace("블럭 제거: ", "");
            Region region = Region.getRegion(id);
            if(region == null) return;

            event.setCancelled(true);
            Material material = event.getCurrentItem().getType();
            region.removeBlock(material);

            inventory.clear();
            Material[] materials = region.getMaterials();
            for(Material m : materials) {

                ItemStack item = new ItemStack(m);
                ItemMeta meta = item.getItemMeta();
                meta.setLore(Collections.singletonList("빈도: " + region.getFrequency(m)));
                item.setItemMeta(meta);

                inventory.addItem(item);
            }
        }
    }

    //좌표 지정 함수
    private void AppointLocation(Player player, Location location, int index) {
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