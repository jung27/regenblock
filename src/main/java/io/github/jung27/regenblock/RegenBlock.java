package io.github.jung27.regenblock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.github.jung27.regenblock.Command.RegenBlockCommand;
import io.github.jung27.regenblock.Event.RegenBlockEventListener;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Region.Region;
import io.github.jung27.regenblock.Region.RegionAdapter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public final class RegenBlock extends JavaPlugin {
    private RegenBlockEventListener regenBlockEventListener;
    private static RegenBlock instance;
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting().registerTypeAdapter(Region.class, new RegionAdapter())
            .create();
    @Override
    public void onEnable() {
        // Plugin startup logic
        regenBlockEventListener = new RegenBlockEventListener();

        getServer().getPluginManager().registerEvents(regenBlockEventListener, this);
        getCommand("regenblock").setExecutor(new RegenBlockCommand());
        load();

        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        save();
    }

    void save() {
        String path = getDataFolder().getAbsolutePath() + "/regions.json";
        File file = new File(path);
        try {
            file.getParentFile().mkdir();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(Region.regions, fileWriter);
            fileWriter.flush();
            fileWriter.close();

            getLogger().info("지역 저장됨.");
        } catch (IOException e) {
            getLogger().info("지역 저장 실패.");
        }
    }

    void load(){
        String path = getDataFolder().getAbsolutePath() + "/regions.json";
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            gson.fromJson(fr, new TypeToken<ArrayList<Region>>(){}.getType());
            getLogger().info("지역 로드됨.");
        } catch (IOException e) {
            getLogger().info("지역 로드 실패.");
        }
    }

    public static RegenBlock instance() {
        return instance;
    }

    public RegenBlockEventListener getRegenBlockEventListener() {
        return regenBlockEventListener;
    }
}
