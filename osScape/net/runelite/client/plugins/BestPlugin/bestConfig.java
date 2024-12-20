package net.runelite.client.plugins.BestPlugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("bestPlugin")
public interface bestConfig extends Config
{
    @ConfigItem(
            keyName = "Message",
            name = "WORD!!!",
            description = "SO true"
    )
    default boolean Message() {
        return true;
    }
}