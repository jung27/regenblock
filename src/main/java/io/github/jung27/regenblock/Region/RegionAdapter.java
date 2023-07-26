package io.github.jung27.regenblock.Region;

import com.cryptomorin.xseries.XMaterial;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegionAdapter extends TypeAdapter<Region> {

    @Override
    public void write(JsonWriter out, Region value) throws IOException {
        if (out == null || value == null) return;
        Gson gson = new Gson();
        Location startLocation = value.getStartLocation();
        Location endLocation = value.getEndLocation();

        LinkedHashMap<String, Integer> frequencies = new LinkedHashMap<>();
        for (Map.Entry<XMaterial, Integer> entry : value.getFrequencies().entrySet()) {
            frequencies.put(entry.getKey().name(), entry.getValue());
        }

        out.beginObject();
        out.name("id").value(value.getId());
        out.name("startLocation").value(startLocation.getWorld().getName()+":"+startLocation.getBlockX()+":"+startLocation.getBlockY()+":"+startLocation.getBlockZ());
        out.name("endLocation").value(endLocation.getWorld().getName()+":"+endLocation.getBlockX()+":"+endLocation.getBlockY()+":"+endLocation.getBlockZ());
        out.name("frequencies").value(gson.toJson(frequencies));
        out.name("regenDelay").value(value.getRegenDelay());
        out.name("expDrop").value(value.isExpDrop());
        out.endObject();
    }

    @Override
    public Region read(JsonReader in) throws IOException {
        if (in == null || in.peek() == JsonToken.NULL) {
            if(in != null) in.nextNull();
            return null;
        }

        String[] locString = null;

        in.beginObject();
        String id = null;
        Location startLocation = null;
        Location endLocation = null;
        LinkedHashMap<XMaterial, Integer> frequencies = new LinkedHashMap<>();
        long regenDelay = 20L;
        boolean expDrop = true;

        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    id = in.nextString();
                    break;
                case "startLocation":
                    locString = in.nextString().split(":");
                    startLocation = new Location(Bukkit.getWorld(locString[0]),
                            Double.parseDouble(locString[1]), Double.parseDouble(locString[2]), Double.parseDouble(locString[3]));
                    break;
                case "endLocation":
                    locString = in.nextString().split(":");
                    endLocation = new Location(Bukkit.getWorld(locString[0]),
                            Double.parseDouble(locString[1]), Double.parseDouble(locString[2]), Double.parseDouble(locString[3]));
                    break;
                case "frequencies":
                    Gson gson = new Gson();
                    LinkedHashMap<String, Integer> sil = gson.fromJson(in.nextString(), new TypeToken<LinkedHashMap<String, Integer>>(){}.getType());
                    for (Map.Entry<String, Integer> entry : sil.entrySet()) {
                        frequencies.put(XMaterial.valueOf(entry.getKey()), entry.getValue());
                    }
                    break;
                case "regenDelay":
                    regenDelay = in.nextLong();
                    break;
                case "expDrop":
                    expDrop = in.nextBoolean();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        Region region = new Region(startLocation, endLocation, id);
        for(Map.Entry<XMaterial, Integer> entry : frequencies.entrySet()) {
            region.setFrequency(entry.getKey(), entry.getValue());
        }
        region.setRegenDelay(regenDelay);
        region.setExpDrop(expDrop);
        return region;
    }
}
