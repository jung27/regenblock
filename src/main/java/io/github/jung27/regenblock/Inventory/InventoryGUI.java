package io.github.jung27.regenblock.Inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class InventoryGUI implements InventoryHandler{
    private Inventory inventory;
    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();

    public Inventory getInventory() {
        if (inventory == null) this.inventory = this.createInventory();
        return inventory;
    }

    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    public void decorate(Player player){
        this.buttonMap.forEach((slot, button) -> this.inventory.setItem(slot, button.getIconCreator().apply(player)));
    }

    public void reload(Player player) {
        this.buttonMap.clear();
        this.inventory.clear();
        this.decorate(player);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getRawSlot();
        InventoryButton button = this.buttonMap.get(slot);
        if(button != null) {
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.decorate((Player) event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {}

    protected abstract Inventory createInventory();
}
