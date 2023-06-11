package io.github.jung27.regenblock;

import io.github.jung27.regenblock.Command.RegenBlockCommand;
import io.github.jung27.regenblock.Event.RegenBlockEventListener;
import io.github.jung27.regenblock.Inventory.GUIManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RegenBlock extends JavaPlugin {
    private RegenBlockEventListener regenBlockEventListener;
    private static RegenBlock instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        regenBlockEventListener = new RegenBlockEventListener();

        getServer().getPluginManager().registerEvents(regenBlockEventListener, this);
        getCommand("regenblock").setExecutor(new RegenBlockCommand());

        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static RegenBlock instance() {
        return instance;
    }

    public RegenBlockEventListener getRegenBlockEventListener() {
        return regenBlockEventListener;
    }
}
