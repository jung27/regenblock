package io.github.jung27.regenblock.Event;

import com.sun.tools.javac.util.Pair;
import io.github.jung27.regenblock.InvetoryHolder.BlockHolder;
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
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location blockLocation = event.getBlock().getLocation();

        //좌표 지정
        if(AppointingPlayers.containsKey(event.getPlayer().getUniqueId())) {

            event.setCancelled(true);
            appointLocation(event.getPlayer(), blockLocation, 0);

        }

        //블럭 리젠
        regenBlock(blockLocation);
    }

    @EventHandler
    public void  onBlockExplode(BlockExplodeEvent event) {
        regenBlock(event.getBlock().getLocation());
    }
    @EventHandler
    public void  onDecay(LeavesDecayEvent event) {
        regenBlock(event.getBlock().getLocation());
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

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String id = playersSettingFre.get(player.getUniqueId()).fst;
        Material material = playersSettingFre.get(player.getUniqueId()).snd;
        if(id == null || material == null) return;
        Region region = Region.getRegion(id);
        int frequency = Integer.parseInt(event.getMessage());

        if(region == null) {
            player.sendMessage("해당 id의 지역이 존재하지 않습니다.");
            return;
        }

        if(frequency <= 0) {
            player.sendMessage("빈도는 0보다 커야 합니다.");
            return;
        }

        region.setFrequency(material, frequency);

        Inventory inv = Bukkit.createInventory(new BlockHolder(), 54, "블럭 편집: " + id);

        Material[] materials = region.getMaterials();
        for(Material m : materials) {

            ItemStack item = new ItemStack(m);
            ItemMeta meta = item.getItemMeta();
            meta.setLore(Collections.singletonList("빈도: " + region.getFrequency(m)));
            item.setItemMeta(meta);

            inv.addItem(item);
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        Inventory inventory = view.getTopInventory();
        if(inventory.getHolder() instanceof BlockHolder){
            String id = inventory.getTitle().replace("블럭 편집: ", "");
            Region region = Region.getRegion(id);
            if(region == null) return;

            event.setCancelled(true);
            Material material = event.getCurrentItem().getType();
            if(event.getClickedInventory() == view.getBottomInventory()){
                region.addBlock(material, 1);
            } else{
                switch (event.getClick()) {
                    case LEFT:
                        region.removeBlock(material);
                        break;
                    case RIGHT:
                        playersSettingFre.put(event.getWhoClicked().getUniqueId(), Pair.of(id, material));
                        event.getWhoClicked().closeInventory();
                        event.getWhoClicked().sendMessage("블럭의 빈도를 입력해주세요.");
                        return;
                    default:
                        return;
                }
            }

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
    private void regenBlock(Location loc){
        for (Region region : Region.regions) {
            if (region.isInside(loc)) {
                Bukkit.getScheduler().runTaskLater(RegenBlock.instance(), () -> loc.getBlock().setType(region.getMaterial()), region.getRegenDelay());
                return;
            }
        }
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