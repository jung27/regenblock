package io.github.jung27.regenblock.Region;

import com.cryptomorin.xseries.XMaterial;
import io.github.jung27.regenblock.RegenBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Region implements Cloneable {
    private Location startLocation;
    private Location endLocation;
    private String id;
    private LinkedHashMap<XMaterial, Integer> frequencies = new LinkedHashMap<>();
    public static ArrayList<Region> regions = new ArrayList<>();
    private long regenDelay = 20L;
    private boolean expDrop = true;

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

    public LinkedHashMap<XMaterial, Integer> getFrequencies() {
        return frequencies;
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
    public XMaterial[] getMaterials() {
        return frequencies.keySet().toArray(new XMaterial[0]);
    }

    public XMaterial getMaterial() {
        int total = 0;
        for (int frequency : frequencies.values()) {
            total += frequency;
        }

        int random = (int) (Math.random() * total);
        int sum = 0;
        for (XMaterial material : frequencies.keySet()) {
            sum += frequencies.get(material);
            if (random < sum) {
                return material;
            }
        }
        return null;
    }
    public int getFrequency(XMaterial material) {
        return frequencies.get(material);
    }

    public void setFrequency(XMaterial material, int frequency) {
        frequencies.put(material, frequency);
    }

    public void addBlock(XMaterial material, int frequency) {
        frequencies.put(material, frequency);
    }
    public void removeBlock(XMaterial material) {
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

    private void setFrequencies(LinkedHashMap<XMaterial, Integer> frequencies) {
        this.frequencies = frequencies;
    }

    public boolean isExpDrop() {
        return expDrop;
    }

    public void setExpDrop(boolean expDrop) {
        this.expDrop = expDrop;
    }
    public void regenAll(){
        //시작 지점부터 끝 지점까지 모두 리젠
        for(int x = Math.min(startLocation.getBlockX(), endLocation.getBlockX()); x <= Math.max(startLocation.getBlockX(), endLocation.getBlockX()); x++){
            for(int y = Math.min(startLocation.getBlockY(), endLocation.getBlockY()); y <= Math.max(startLocation.getBlockY(), endLocation.getBlockY()); y++){
                for(int z = Math.min(startLocation.getBlockZ(), endLocation.getBlockZ()); z <= Math.max(startLocation.getBlockZ(), endLocation.getBlockZ()); z++){
                    Location loc = new Location(startLocation.getWorld(), x, y, z);
                    regenBlock(loc);
                }
            }
        }
    }
    public static Region regenBlock(Location loc){
        for (Region region : regions) {
            if(region.frequencies.size() == 0) return null;
            if (region.isInside(loc)) {
                Bukkit.getScheduler().runTaskLater(RegenBlock.instance(), () -> {
                    XMaterial m = region.getMaterial();
                    Block block = loc.getBlock();
                    block.setType(m.parseMaterial());
                    BlockState state = block.getState();
                    MaterialData data = state.getData();
                    data.setData(m.getData());
                    state.update();
                }, region.getRegenDelay());
                return region;
            }
        }
        return null;
    }

    public Region clone() {
        System.out.println(regions.size());
        Object obj = null;
        try {
            obj = super.clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Region region = (Region) obj;
        if(region == null) return null;
        region.setStartLocation(startLocation.clone());
        region.setEndLocation(endLocation.clone());
        LinkedHashMap<XMaterial, Integer> frequencies = new LinkedHashMap<>(this.frequencies);
        region.setFrequencies(frequencies);
        region.setId(id+" (복사본)");
        regions.add(region);

        System.out.println(regions.size());

        return region;
    }
}
