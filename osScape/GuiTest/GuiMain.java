package GuiTest;

import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.robot.script.Script;

import java.awt.*;

@ScriptManifest(author = "alex", category = Category.OTHER, description = "", discord = "",
        name = "TEST", servers = { "osrsps" }, version = "0.1")

public class GuiMain extends Script {
    private GuiGui gui;
    @Override
    public void onChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onExecute() {
        // Initialize the GUI
        gui = new GuiGui();

        // Show the GUI
        gui.show();

    }

    @Override
    public void onProcess() {

    }

    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.close();
        }

    }

    @Override
    public void paint(Graphics graphics) {

    }
}
