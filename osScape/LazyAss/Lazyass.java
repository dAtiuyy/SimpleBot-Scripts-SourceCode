package LazyAss;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.robot.script.Script;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "",
        name = "LAZy", servers = { "osrsps" }, version = "0.1")

public class Lazyass extends Script {
    public String status;
    public long startTime;
    public boolean invFull, chiselVar;
    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }

    @Override
    public void onExecute() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        SimpleItem item1 = ctx.inventory.populate().filter(2048).next(); // chisel
        SimpleItem item2 = ctx.inventory.populate().filter(314).next(); // amethyst ore
        if (item1 != null && item2 != null) {
            ctx.menuActions.sendAction(58, 1, 9764864, 0, "Use", "<col=ff9040>Feather</col><col=ffffff> -> <col=ff9040>Amethyst dart tip</col>");
        }
    }

    @Override
    public void onTerminate() {}
    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        g.setColor(Color.BLACK);
        g.fillRect(5, 2, 192, 58);
        g.setColor(Color.decode("#ea411c"));
        g.drawRect(5, 2, 192, 58);
        g.drawLine(8, 24, 194, 24);

        g.setColor(Color.decode("#e0ad01"));
        g.drawString("Skill                             v. " + "0.1", 12, 20);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 42);
        g.drawString("Status: " + status, 14, 56);
    }
    public void status(final String status) {
        this.status = status;
    }
}
