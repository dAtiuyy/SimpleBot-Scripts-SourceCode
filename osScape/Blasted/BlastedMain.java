package Blasted;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;

import javax.swing.*;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "",
        name = "Blasted Furnace", servers = { "osrsps" }, version = "0.1")

public class BlastedMain extends Script {
    public String status;
    public long startTime,lastActionTime;
    public int WAIT_TIME_MS = 1350;
    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }

    @Override
    public void onExecute() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        status("in da first");
        SimpleItem IcyGloves = ctx.inventory.populate().filter(1580).next();
        long currentTime = System.currentTimeMillis();
        int stallID = 37969;
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS) {

            SimpleObject stall = ctx.objects.populate().filter(stallID).nearest().next();
            if (stall != null && stall.validateInteractable()) {
                status("doing stuff");
                stall.menuAction("Chop down");
                lastActionTime = currentTime;
                ctx.onCondition(() -> ctx.players.getLocal().getAnimation() != -1, 500, 8);
            }
        } else {
            ctx.sleep(500);
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
        g.drawString("Blasted Furnace                             v. " + "0.1", 12, 20);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 42);
        g.drawString("Status: " + status, 14, 56);
    }
    public void status(final String status) {
        this.status = status;
    }
}
