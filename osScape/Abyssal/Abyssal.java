package Abyssal;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayUtil;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "", name = "Abyssal", servers = {
        "osrsps" }, version = "0.1")

public class Abyssal extends Script implements LoopingScript {

    public String status;
    public long startTime;
    ArrayList<WorldPoint> occupiedTiles;
    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
    }

    String[] stringIDs = {
            String.valueOf(Pattern.compile("Prayer potion\\(\\d+\\)")),
            String.valueOf(Pattern.compile("Super restore\\(\\d+\\)"))};

    @Override
    public void onProcess() {
        SimpleItem food = ctx.inventory.populate().filterHasAction("Eat").next();
        SimpleItem pots = ctx.inventory.populate().filter(stringIDs).next();
        LocalPoint playerLocalPoint = ctx.players.getLocal().getPlayer().getLocalLocation();
        if (ctx.players.getLocal().getHealth() <= 60 && food != null) {
            status("EAT");
            food.menuAction("Eat");
        }

        if (ctx.prayers.points() <= 30 && pots != null) {
            status("DRINK");
            pots.menuAction("Drink");
        }

        // Filter objects with ID 1275 and process them
        ctx.objects.getGraphicsObjects().stream()
                .filter(obj -> obj.getId() == 1275) // Filter objects with ID 1275
                .forEach(obj -> {
                    LocalPoint localPoint = obj.getLocation();
                    moveClosestTile(localPoint);
                });
    }

    public void moveClosestTile(LocalPoint point) {
        WorldPoint objectLocation = WorldPoint.fromLocal(ctx.getClient(), point);
        occupiedTiles = new ArrayList<>();
        occupiedTiles.add(objectLocation);

        if (occupiedTiles.contains(ctx.players.getLocal().getLocation())) {
            int rand = ctx.randomNumber(4);
            switch (rand) {
                case 1:
                    ctx.menuActions.step(new WorldPoint(objectLocation.getX(), objectLocation.getY() + 2, objectLocation.getPlane()));
                    break;
                case 2:
                    ctx.menuActions.step(new WorldPoint(objectLocation.getX() - 2, objectLocation.getY(), objectLocation.getPlane()));
                    break;
                case 3:;
                    ctx.menuActions.step(new WorldPoint(objectLocation.getX(), objectLocation.getY() - 2, objectLocation.getPlane()));
                    break;
                case 4:
                    ctx.menuActions.step(new WorldPoint(objectLocation.getX() + 2, objectLocation.getY(), objectLocation.getPlane()));
                    break;
            }
        }
    }



    @Override
    public void onChatMessage(ChatMessage msg) {
    }
    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
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
        Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
        g.setFont(font1);
        g.drawString(Objects.requireNonNullElse(status, "Null"), 50, 300);
        g.drawString(ctx.paint.formatTime(System.currentTimeMillis() - startTime), 45, 320);
        ctx.objects.getGraphicsObjects().forEach(obj -> {
            LocalPoint localPoint = obj.getLocation();
            ctx.paint.drawTileMatrix(g, WorldPoint.fromLocal(ctx.getClient(), localPoint), Color.BLACK);
        });
        if (occupiedTiles != null && !occupiedTiles.isEmpty()) {
            for (WorldPoint wp : occupiedTiles) {
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