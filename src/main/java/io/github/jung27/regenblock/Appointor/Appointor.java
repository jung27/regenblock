package io.github.jung27.regenblock.Appointor;

import io.github.jung27.regenblock.RegenBlock;
import io.github.jung27.regenblock.Setting.GlobalSetting;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Appointor {
    Player player = null;
    String id = null;
    Location[] locations = {null, null};
    boolean isRunning = false;

    protected Appointor(Player player, String id){
        this.player = player;
        this.id = id;
    }

    public void run(){
        isRunning = true;
        RegenBlock.instance().getRegenBlockEventListener().addAppointor(player.getUniqueId(), this);
    }
    public void appointFirst(Location loc){
        System.out.println(isRunning);
        if (loc == null || !isRunning) return;

        locations[0] = loc;
        player.sendMessage("첫번째 좌표가 지정되었습니다.");
        if (locations[1] != null) appointComplete();
    }
    public void appointSecond(Location loc){
        if (loc == null || !isRunning) return;

        locations[1] = loc;
        player.sendMessage("두번째 좌표가 지정되었습니다.");
        if (locations[0] != null) appointComplete();
    }
    void appointComplete(){
        stop();
        System.out.println(isRunning);
        appoint();
    }

    void stop(){
        isRunning = false;
        RegenBlock.instance().getRegenBlockEventListener().removeAppointor(player.getUniqueId());
    }

    abstract void appoint();
}
