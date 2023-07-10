package io.github.jung27.regenblock.Appointor;

import io.github.jung27.regenblock.Region.Region;
import org.bukkit.entity.Player;

public class ModifyAppointor extends Appointor{
    public ModifyAppointor(Player player, String id) {
        super(player, id);
    }

    @Override
    void appoint() {
        modifyRegion();
        player.sendMessage("지역이 수정되었습니다.");
    }

    private void modifyRegion() {
        Region region = Region.regions.stream().filter(r -> r.getId().equals(id)).findAny().orElse(null);
        if(region == null) return;

        region.setStartLocation(locations[0]);
        region.setEndLocation(locations[1]);
    }
}
