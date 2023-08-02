package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.Appointor.CreateAppointor;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.entity.Player;

public class AppointCommand extends SubCommand {
    @Override
    public String getName() {
        return "appoint";
    }

    @Override
    public String getDescription() {
        return "명령어 입력 후 우클릭한 좌표와 좌클릭한 좌표의 사이 구간을 지정합니다.";
    }

    @Override
    public String getSyntax() {
        return "/regenblock appoint <id>";
    }

    @Override
    public void perform(Player player, String[] args) {
        if(args.length != 2) {
            player.sendMessage("사용법: " + getSyntax());
            return;
        }


        if (Region.getRegion(args[1]) != null) {
            player.sendMessage("이미 존재하는 id입니다.");
            return;
        }

        player.sendMessage("좌클릭과 우클릭으로 두 지점을 지정해주세요.");
        CreateAppointor appointor = new CreateAppointor(player, args[1]);
        appointor.run();
    }
}
