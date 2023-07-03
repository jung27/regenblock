package io.github.jung27.regenblock.Region;

import org.bukkit.Material;

import java.util.ArrayList;

public class RegenMaterial {
    private final Material material;
    private final byte data;
    private static final ArrayList<RegenMaterial> regenMaterials = new ArrayList<>();
    public static RegenMaterial get(Material material, byte data) {
        return regenMaterials.stream().filter(rm -> rm.getMaterial() == material && rm.getData() == data).findFirst().orElseGet(() -> {
            RegenMaterial regenMaterial = new RegenMaterial(material, data);
            regenMaterials.add(regenMaterial);
            return regenMaterial;
        });
    }

    public static RegenMaterial get(Material material) {
        return get(material, (byte) 0);
    }

    private RegenMaterial(Material material, byte data) {
        this.material = material;
        this.data = data;
    }

    public Material getMaterial() {
        return material;
    }


    public byte getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RegenMaterial{" +
                "material=" + material +
                ", data=" + data +
                '}';
    }
}
