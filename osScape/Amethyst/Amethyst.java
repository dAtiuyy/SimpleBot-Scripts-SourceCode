package Amethyst;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "",
        name = "Amethyst", servers = { "osrsps" }, version = "0.1")

public class Amethyst extends Script {
    public String status;
    public long startTime,lastActionTime;
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
        SimpleItem useItem = ctx.inventory.populate().filter(1755).next(); // chisel
        SimpleItem amethystItem = ctx.inventory.populate().filter(21347).next(); // amethyst ore
        SimpleObject spot = ctx.objects.populate().filter(11388).nearest().next();
        long currentTime = System.currentTimeMillis();
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > 1500) {
            if (spot != null && !chiselVar) {
                if (ctx.inventory.inventoryFull()) invFull = true;
                status("doing stuff");
                spot.menuAction("Mine");
                lastActionTime = currentTime;
                ctx.onCondition(() -> ctx.players.getLocal().getAnimation() != -1, 500, 8);
            }
            if (useItem != null && ctx.widgets.getWidget(270,13) == null && invFull) {
                if (amethystItem != null) {
                    chiselVar = true;
                } else {
                    chiselVar = false;
                    invFull = false;
                }
                ctx.menuActions.sendAction(25, 0, 9764864, 0, "Use", "<col=ff9040>Chisel</col>");
                ctx.sleep(25, 75);
                ctx.menuActions.sendAction(58, 2, 9764864, 0, "Use", "<col=ff9040>Chisel</col><col=ffffff> -> <col=ff9040>Amethyst</col>");
            }
            if (ctx.widgets.getWidget(270,13) != null) {
                ctx.menuActions.sendAction(57, -1, 17694734, 1, "Make", "<col=ff9040>15 Amethyst bolt tips</col>");
            }
        } else {
            ctx.sleep(1000);
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
