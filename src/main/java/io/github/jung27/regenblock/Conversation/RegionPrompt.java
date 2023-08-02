package io.github.jung27.regenblock.Conversation;

import io.github.jung27.regenblock.Appointor.CreateAppointor;
import io.github.jung27.regenblock.GUI.RegionGUI;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class RegionPrompt extends StringPrompt {
    public RegionPrompt() {
        super();
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        Conversable p = context.getForWhom();
        if (Region.getRegion(input) != null) {
            p.sendRawMessage("이미 존재하는 id입니다.");
            return null;
        }
        p.sendRawMessage("좌클릭과 우클릭으로 두 지점을 지정해주세요.");
        CreateAppointor appointor = new CreateAppointor((Player) p, input);
        appointor.run();
        return null;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "생성할 지역의 id를 입력하세요.";
    }
}
