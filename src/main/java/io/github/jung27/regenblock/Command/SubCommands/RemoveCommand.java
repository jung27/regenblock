package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.entity.Player;

public class RemoveCommand extends SubCommand {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "해당 id의 지역을 제거합니다.";
    }

    @Override
    public String getSyntax() {
        return "/regenblock remove <id>";
    }

    @Override
    public void perform(Player player, String[] args) {
        Region region = Region.getRegion(args[1]);
        if(region == null) {
            player.sendMessage("해당 id의 지역이 존재하지 않습니다.");
            return;
        }

        Region.regions.remove(region);
        player.sendMessage("지역이 삭제되었습니다.");
    }
}
