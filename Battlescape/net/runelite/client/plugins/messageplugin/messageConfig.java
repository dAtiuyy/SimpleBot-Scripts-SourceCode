package net.runelite.client.plugins.messageplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("message")
public interface messageConfig extends Config
        {
@ConfigItem(
        keyName = "greeting",
        name = "What print",
        description = "Hopefully this will turn into an auto-protect plugin"
)

default String greeting() {
        return "Hello";
}

@ConfigItem(
        keyName = "Second_message",
        name = "Second Message what print",
        description = "what is say when u hover it lol"
)

default String Second_message() {
        return "Hello";
}
        }