package io.github.jung27.regenblock.Appointor;

import io.github.jung27.regenblock.Region.Region;
import org.bukkit.entity.Player;

public class CreateAppointor extends Appointor{
    public CreateAppointor(Player player, String id) {
        super(player, id);
    }

    @Override
    void appoint() {
        createRegion();
        player.sendMessage("지역이 생성되었습니다.");
    }

    void createRegion() {
        new Region(locations[0], locations[1], id);
    }
}
