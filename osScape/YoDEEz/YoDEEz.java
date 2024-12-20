package YoDEEz;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

import java.awt.*;
import java.util.*;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "", name = "YoDEEz", servers = {
        "osrsps" }, version = "0.1")

public class YoDEEz extends Script implements LoopingScript {
    WorldArea combatArea, bossArea;
    ArrayList<WorldPoint> availableTiles, removeTiles;
    public String status;
    public long startTime;
    public int[] IDsTEK = {7540, 7541, 7542, 7543};
    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
        WorldPoint anchor = ctx.objects.populate().filter(29867).nextNearest().getLocation();
        combatArea = new WorldArea(
                new WorldPoint(anchor.getX() - 7, anchor.getY() - 1, anchor.getPlane()),
                new WorldPoint(anchor.getX() + 6, anchor.getY() - 13, anchor.getPlane())
        );
        availableTiles = new ArrayList<>();
        availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
    }

    @Override
    public void onProcess() {
        updateAvailableTiles();
    }

    private void updateAvailableTiles() {
        if (!ctx.npcs.populate().filter(IDsTEK).isEmpty()) {
            SimpleNpc bossNpc = ctx.npcs.populate().filter(IDsTEK).nextNearest();
            if (bossNpc != null) {
                WorldPoint anchor2 = bossNpc.getLocation();
                bossArea = new WorldArea(
                        new WorldPoint(anchor2.getX(), anchor2.getY() + 4, anchor2.getPlane()),
                        new WorldPoint(anchor2.getX() + 4, anchor2.getY(), anchor2.getPlane())
                );
                removeTiles = new ArrayList<>(Arrays.asList(bossArea.getWorldPoints()));
                availableTiles.removeAll(removeTiles);
            }
        } else {
            availableTiles.clear();
            availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
        }
    }


    @Override
    public void onChatMessage(ChatMessage msg) {
    }

    @Override
    public void onTerminate() {}

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.decode("#e0ad01"));
        g.drawString("Status: ", 10, 300);
        g.drawString("Time: ", 10, 320);
        g.setColor(Color.RED);
        Font font1 = new Font("Karla", Font.BOLD, 10);
        g.setFont(font1);
        g.drawString(Objects.requireNonNullElse(status, "Null"), 50, 300);
        g.drawString(ctx.paint.formatTime(System.currentTimeMillis() - startTime), 45, 320);
        if (availableTiles != null && !availableTiles.isEmpty()) {
            for (WorldPoint wp : availableTiles) {
                ctx.paint.drawTileMatrix(g, wp, Color.GREEN);
            }
        }
        if (removeTiles != null && !removeTiles.isEmpty()) {
            for (WorldPoint wp : removeTiles) {
                ctx.paint.drawTileMatrix(g, wp, Color.RED);
            }
        }
    }

    @Override
    public int loopDuration() {
        return 100;
    }
    public void status(final String status) {
        this.status = status;
    }
}