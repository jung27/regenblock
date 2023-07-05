package io.github.jung27.regenblock.Conversation;

import io.github.jung27.regenblock.GUI.RegionGUI;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class IdPrompt extends StringPrompt {
    private final Region region;
    public IdPrompt(Region region) {
        super();
        this.region = region;
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Conversable p = context.getForWhom();
        p.sendRawMessage("id가 " + input + "(으)로 설정되었습니다.");
        try {
            region.setId(input);
        } catch (IllegalArgumentException e) {
            p.sendRawMessage("id가 이미 존재합니다.");
            return this;
        }
        GUIManager.getInstance().openGUI(new RegionGUI(region), (Player) p);
        return null;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "id를 입력하세요.";
    }
}
