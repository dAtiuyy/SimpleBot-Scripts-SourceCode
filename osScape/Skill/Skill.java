package Skill;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.queries.SimpleEntityQuery;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.hooks.simplebot.Paintable;

import javax.swing.*;

@ScriptManifest(author = "alex", category = Category.OTHER, description = "", discord = "",
        name = "Skill", servers = { "osrsps" }, version = "0.1")

public class Skill extends Script {
    private Set<String> oldNPCs = new HashSet<>();
    public int[] npcIds;
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
        SimpleItem seedPod = ctx.inventory.populate().filter("Seed pod").next();
        SimplePrayers prayers = new SimplePrayers(ctx);
        SimpleItem potion = ctx.inventory.populate().filter(Pattern.compile("Prayer potion\\(\\d+\\)")).next();

        long currentTime = System.currentTimeMillis();
        // Determine which stall ID to use based on the thieving level
        //int stallID = getSettings().getThieveSelection().getId();
        int stallID = 37969;

        // Check if the player is idling and if the last action was more than 5 seconds ago
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS) {

            SimpleObject stall = ctx.objects.populate().filter(stallID).nearest().next();
            if (stall != null && stall.validateInteractable()) {
                status("doing stuff");
                stall.menuAction("Chop down");
                lastActionTime = currentTime; // Update the last action time
                ctx.onCondition(() -> ctx.players.getLocal().getAnimation() != -1, 500, 8); // Wait until the player is not idle
            }
        } else {
            ctx.sleep(500); // Sleep for a bit before checking again
        }
    }

    public void npcListToLog() {
        SimpleEntityQuery<SimpleNpc> npcs = ctx.npcs.populate();
        npcs.filterWithin(10);//within 10 tiles
        npcs.filterHasAction("Attack");//must have attack as an option
        Set<String> nowNPCs = new HashSet<>();
        for (SimpleNpc npc : npcs) {
            String npcName = npc.getName();
            nowNPCs.add(npcName);
        }
        if (!nowNPCs.equals(oldNPCs)) {
            for (String enemyName : nowNPCs) {
                System.out.println("Enemy Name: " + enemyName);
            }
            oldNPCs = nowNPCs;
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
