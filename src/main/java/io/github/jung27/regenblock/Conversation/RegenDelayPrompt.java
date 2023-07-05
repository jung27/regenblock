package io.github.jung27.regenblock.Conversation;

import io.github.jung27.regenblock.GUI.RegionGUI;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class RegenDelayPrompt extends NumericPrompt {
    private final Region region;
    public RegenDelayPrompt(Region region) {
        super();
        this.region = region;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        Conversable p = context.getForWhom();
        p.sendRawMessage("리젠 딜레이가 " + String.format("%.2f", (input.intValue()/20.0)) + "초로 설정되었습니다.");
        region.setRegenDelay(input.longValue());
        GUIManager.getInstance().openGUI(new RegionGUI(region), (Player) p);
        return null;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "리젠 딜레이를 입력하세요.";
    }
}
