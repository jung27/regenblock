package io.github.jung27.regenblock.Conversation;

import com.cryptomorin.xseries.XMaterial;
import io.github.jung27.regenblock.GUI.BlockGUI;
import io.github.jung27.regenblock.Inventory.GUIManager;
import io.github.jung27.regenblock.Region.Region;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class FrequencyPrompt extends NumericPrompt {
    private final Region region;
    private final XMaterial material;
    public FrequencyPrompt(Region region, XMaterial material) {
        super();
        this.region = region;
        this.material = material;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        Conversable p = context.getForWhom();
        p.sendRawMessage("빈도가 " + input + "(으)로 설정되었습니다.");
        region.setFrequency(material, input.intValue());
        GUIManager.getInstance().openGUI(new BlockGUI(region), (Player) p);
        return null;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "빈도를 입력하세요.";
    }
}
