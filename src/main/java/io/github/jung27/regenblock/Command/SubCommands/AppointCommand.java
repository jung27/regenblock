package io.github.jung27.regenblock.Command.SubCommands;

import io.github.jung27.regenblock.Command.SubCommand;
import io.github.jung27.regenblock.RegenBlock;
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

        player.sendMessage("좌클릭과 우클릭으로 두 지점을 지정해주세요.");

        RegenBlock.instance().getRegenBlockEventListener().addAppointingPlayer(player, args[1]);
    }
}
