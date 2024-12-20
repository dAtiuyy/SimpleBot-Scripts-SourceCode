package Hydra;

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

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "", name = "Hydra", servers = {
        "osrsps" }, version = "0.1")

public class Hydra extends Script implements LoopingScript {
    WorldArea combatArea;
    ArrayList<WorldPoint> availableTiles;
    public String status;
    public long startTime;
    public int[] PotsIds = {143, 3030, 141, 3028, 139, 3026, 2434, 3024};
    public int[] ID = {34562, 34563, 34564};
    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
        WorldPoint anchor = ctx.objects.populate().filter(34586).nextNearest().getLocation();
        combatArea = new WorldArea(
                new WorldPoint(anchor.getX() + 5, anchor.getY() - 12, anchor.getPlane()),
                new WorldPoint(anchor.getX() + 26, anchor.getY() + 10, anchor.getPlane())
        );
        availableTiles = new ArrayList<>();
        availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
        if (!ctx.objects.populate().filter(ID).isEmpty()) {
            for (SimpleObject obj : ctx.objects.populate().filter(ID)) {
                WorldPoint objectLocation = obj.getLocation();
                List<WorldPoint> occupiedTiles = new ArrayList<>();
                occupiedTiles.add(objectLocation);
                occupiedTiles.add(new WorldPoint(objectLocation.getX() + 1, objectLocation.getY(), objectLocation.getPlane()));
                occupiedTiles.add(new WorldPoint(objectLocation.getX(), objectLocation.getY() + 1, objectLocation.getPlane()));
                occupiedTiles.add(new WorldPoint(objectLocation.getX() + 1, objectLocation.getY() + 1, objectLocation.getPlane()));
                availableTiles.removeAll(occupiedTiles);
            }
        } else {
            availableTiles.removeAll(availableTiles);
            availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
        }
    }

    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filter("Alchemical Hydra").nextNearest();
        SimpleItem food = ctx.inventory.populate().filterHasAction("Eat").next();
        SimpleItem pots = ctx.inventory.populate().filter(PotsIds).next();
        if (ctx.players.getLocal().getHealth() <= 60 && food != null) {
            status("EAT");
            food.menuAction("Eat");
        }

        if (ctx.prayers.points() <= 60 && pots != null) {
            status("DRINK");
            pots.menuAction("Drink");
        }
        if (target != null) {
            if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR) && ctx.prayers.points() > 0) {
                ctx.menuActions.sendAction(57, -1 , 35455009, 1, "Activate", "<col=ff9040>Rigour</col>");
            }
        } else {
            if (ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR)) {
                ctx.menuActions.sendAction(57, -1 , 35455009, 1, "Deactivate", "<col=ff9040>Rigour</col>");
            }
            if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                ctx.menuActions.sendAction(57, -1, 35454997, 1, "Deactivate", "<col=ff9040>Protect From Magic</col>");
            }
            if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                ctx.menuActions.sendAction(57, -1, 35454998, 1, "Deactivate", "<col=ff9040>Protect From Missiles</col>");
            }
        }
    }

    @Override
    public void onChatMessage(ChatMessage msg) {
    }
    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        List<Projectile> projectiles = ctx.projectiles.getActiveProjectiles();
        Optional<Projectile> closestProjectile = projectiles.stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(Projectile::getRemainingCycles));

        closestProjectile.ifPresent(projectile -> {
            if (ctx.prayers.points() > 0) {
                if (!ctx.npcs.populate().filter("Alchemical Hydra").isEmpty()) {
                    if (projectile.getId() == 1662 && !ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                        ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>Protect From Magic</col>");
                    } else if (projectile.getId() == 1663 && !ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                        ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>Protect From Missiles</col>");
                    }
                }
            }
        });
    }

    private void renderTileObject(Graphics2D graphics, TileObject tileObject, Player player, Color color) {
        if (tileObject != null && player.getLocalLocation().distanceTo(tileObject.getLocalLocation()) <= 2400) {
            OverlayUtil.renderTileOverlay(graphics, tileObject, "ID: " + tileObject.getId(), color);
        }
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
        if (availableTiles != null && !availableTiles.isEmpty()) {
            for (WorldPoint wp : availableTiles) {
                //ctx.paint.drawTileMatrix(g, wp, Color.GREEN);
            }
        }
        ctx.objects.getGraphicsObjects().forEach(obj -> {
            LocalPoint localPoint = obj.getLocation();
            ctx.paint.drawTileMatrix(g, WorldPoint.fromLocal(ctx.getClient(), localPoint), Color.BLACK);
        });
    }

    @Override
    public int loopDuration() {
        return 100;
    }
    public void status(final String status) {
        this.status = status;
    }
}