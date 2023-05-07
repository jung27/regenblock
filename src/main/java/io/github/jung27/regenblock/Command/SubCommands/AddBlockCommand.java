package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class AddBlockCommand extends SubCommand {
    @Override
    public String getName() {
        return "addblock";
    }

    @Override
    public String getDescription() {
        return "손에 들고있는 블럭을 해당 지역에 출현하는 블럭에 추가합니다.";
    }

    @Override
    public String getSyntax() {
        return "/regenblock addblock <id> <빈도>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length != 3) {
            player.sendMessage("사용법: " + getSyntax());
            return;
        }

        Material material = player.getInventory().getItemInMainHand().getType();
        if(material == null || !material.isBlock()) {
            player.sendMessage("블럭을 들고 있어야 합니다.");
            return;
        }

        int frequency;
        try {
            frequency = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            player.sendMessage("빈도는 숫자여야 합니다.");
            return;
        }

        if(frequency < 1) {
            player.sendMessage("빈도는 1 이상이어야 합니다.");
            return;
        }

        Region region = Region.getRegion(args[1]);
        if(region == null) {
            player.sendMessage("해당 id의 지역이 존재하지 않습니다.");
            return;
        }

        region.addBlock(material, frequency);
        player.sendMessage("블럭이 추가되었습니다.");
    }
}
