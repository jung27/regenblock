package io.github.jung27.regenblock.Region;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class Region{
    private final Location startLocation;
    private final Location endLocation;
    private final String id;
    private final HashMap<Material, Integer> frequencies = new HashMap<>();
    public static ArrayList<Region> regions = new ArrayList<>();

    public Region(Location start, Location end, String id) {
        this.startLocation = start;
        this.endLocation = end;
        this.id = id;

        regions.add(this);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public String getId() {
        return id;
    }
    public Material[] getMaterials() {
        return frequencies.keySet().toArray(new Material[0]);
    }

    public Material getMaterial() {
        int total = 0;
        for (int frequency : frequencies.values()) {
            total += frequency;
        }

        int random = (int) (Math.random() * total);
        int sum = 0;
        for (Material material : frequencies.keySet()) {
            sum += frequencies.get(material);
            if (random < sum) {
                return material;
            }
        }
        return Material.AIR;
    }
    public int getFrequency(Material material) {
        return frequencies.get(material);
    }

    public void addBlock(Material material, int frequency) {
        frequencies.put(material, frequency);
    }
    public void removeBlock(Material material) {
        frequencies.remove(material);
    }

    public boolean isInside(Location location) {
        return location.getX() >= startLocation.getX() && location.getX() <= endLocation.getX() &&
                location.getY() >= startLocation.getY() && location.getY() <= endLocation.getY() &&
                location.getZ() >= startLocation.getZ() && location.getZ() <= endLocation.getZ();
    }

    public static Region getRegion(String id) {
        for (Region region : regions) {
            if (region.getId().equals(id)) {
                return region;
            }
        }
        return null;
    }
}
