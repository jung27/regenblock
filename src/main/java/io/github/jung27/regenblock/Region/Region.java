package io.github.jung27.regenblock.Region;

import io.github.jung27.regenblock.RegenBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Region implements Cloneable {
    private Location startLocation;
    private Location endLocation;
    private String id;
    private final LinkedHashMap<RegenMaterial, Integer> frequencies = new LinkedHashMap<>();
    public static ArrayList<Region> regions = new ArrayList<>();
    private Long regenDelay = 20L;

    public Region(Location start, Location end, String id) {
        this.startLocation = start;
        this.endLocation = end;
        this.id = id;

        regions.add(this);
    }

    public Location getStartLocation() {
        return startLocation;
    }
    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }
    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public String getId() {
        return id;
    }
    public RegenMaterial[] getMaterials() {
        return frequencies.keySet().toArray(new RegenMaterial[0]);
    }

    public RegenMaterial getMaterial() {
        int total = 0;
        for (int frequency : frequencies.values()) {
            total += frequency;
        }

        int random = (int) (Math.random() * total);
        int sum = 0;
        for (RegenMaterial material : frequencies.keySet()) {
            sum += frequencies.get(material);
            if (random < sum) {
                return material;
            }
        }
        return RegenMaterial.get(Material.AIR);
    }
    public int getFrequency(RegenMaterial material) {
        return frequencies.get(material);
    }

    public void setFrequency(RegenMaterial material, int frequency) {
        frequencies.put(material, frequency);
    }

    public void addBlock(RegenMaterial material, int frequency) {
        frequencies.put(material, frequency);
    }
    public void removeBlock(RegenMaterial material) {
        frequencies.remove(material);
    }

    public boolean isInside(Location location) {
        return location.getWorld().equals(startLocation.getWorld()) &&
                location.getX() >= Math.min(startLocation.getX(), endLocation.getX()) && location.getX() <= Math.max(startLocation.getX(), endLocation.getX()) &&
                location.getY() >= Math.min(startLocation.getY(), endLocation.getY()) && location.getY() <= Math.max(startLocation.getY(), endLocation.getY()) &&
                location.getZ() >= Math.min(startLocation.getZ(), endLocation.getZ()) && location.getZ() <= Math.max(startLocation.getZ(), endLocation.getZ());
    }

    public Long getRegenDelay() {
        return regenDelay;
    }

    public void setRegenDelay(Long regenDelay) {
        this.regenDelay = regenDelay;
    }

    public static Region getRegion(String id) {
        for (Region region : regions) {
            if (region.getId().equals(id)) {
                return region;
            }
        }
        return null;
    }

    public void setId(String id) {
        for (Region region : regions) {
            if (region.getId().equals(id)) {
                throw new IllegalArgumentException("Region with id " + id + " already exists");
            }
        }
        this.id = id;
    }

    public static void regenBlock(Location loc){
        for (Region region : regions) {
            if (region.isInside(loc)) {
                Bukkit.getScheduler().runTaskLater(RegenBlock.instance(), () -> {
                    RegenMaterial m = region.getMaterial();
                    loc.getBlock().setType(m.getMaterial());
                    loc.getBlock().setData(m.getData());
                }, region.getRegenDelay());
                return;
            }
        }
    }

    public Region clone() {
        Region clone = null;
        try {
            clone = (Region) super.clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
