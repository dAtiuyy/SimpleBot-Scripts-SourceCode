package net.runelite.client.plugins.BestPlugin;

import java.awt.event.KeyEvent;
import javax.inject.Inject;

import net.runelite.api.events.ChatMessage;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.runenergy.RunEnergyConfig;
import net.runelite.client.ui.ClientUI;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ScriptCallbackEvent;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemVariationMapping;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.runelite.client.plugins.BestPlugin.bestConfig;

@PluginDescriptor(
        name = "best plugin",
        description = "Automatically attacks NPCs and banks, eats food, and uses potions",
        tags = {"Hello", "Whats good"},
        enabledByDefault = false
)

public class bestPlugin extends Plugin implements KeyListener {
    @Inject
    private ChatMessageManager chatMessageManager;
    @Inject
    private Client client;
    @Inject
    private bestConfig bestConfig;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private KeyManager keyManager;
    @Inject
    private ConfigManager configManager;
    private boolean messageSent;

    @Override
    protected void startUp() {
        keyManager.registerKeyListener(this);
    }

    @Override
    protected void shutDown() throws Exception{
        keyManager.unregisterKeyListener(this);
        this.messageSent = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No implementation needed for keyTyped in this case
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Check if the key '1' is pressed
        if (e.getKeyCode() == KeyEvent.VK_1) {
            messageSent = false;
        }
    }
    @Provides
    bestConfig getConfig(ConfigManager configManager) {
        return (bestConfig)configManager.getConfig(bestConfig.class);
    }
    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (!this.messageSent && this.bestConfig.Message()) {
            String chatMessage = (new ChatMessageBuilder()).append(ChatColorType.HIGHLIGHT).append("Your Ring of endurance now has less than 500 charges. Add more charges to regain its passive stamina effect.").build();
            this.chatMessageManager.queue(QueuedMessage.builder().type(ChatMessageType.CONSOLE).runeLiteFormattedMessage(chatMessage).build());
            this.messageSent = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No action needed when key is released
    }
}
