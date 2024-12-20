package JadZ;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;

import java.awt.*;
import java.awt.Point;
import java.util.*;
import java.util.List;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "", name = "JadZ", servers = {
        "osrsps" }, version = "0.1")

public class JadZ extends Script implements LoopingScript {
    public String status;
    public boolean Var;
    public int[] PotsIds = {143, 3030, 141, 3028, 139, 3026, 2434, 3024};
    public int[] meleeGearIDs = {6570, 19675, 10551, 19722, 11840};
    public int[] rangeGearIDs = {12926, 22109, 10370, 19936};
    public String[] actions = {"Wield", "Wear"};
    @Override
    public void onExecute() {
    }

    @Override
    public void onProcess() {
        checkClosestEntity();
    }

    public void checkClosestEntity() {
        // Get the player's location
        LocalPoint playerLocation = ctx.getClient().getLocalPlayer().getLocalLocation();

        // Find the closest projectile to the player
        List<Projectile> projectiles = ctx.projectiles.getActiveProjectiles();
        Optional<Projectile> closestProjectile = projectiles.stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(p -> p.getRemainingCycles()));

        // Find the closest graphics object to the player
        List<GraphicsObject> graphicsObjects = ctx.objects.getGraphicsObjects();
        Optional<GraphicsObject> closestGraphicsObject = graphicsObjects.stream()
                .min(Comparator.comparingInt(g -> playerLocation.distanceTo(g.getLocation())));

        // Compare the distances if both are present
        if (closestProjectile.isPresent() && closestGraphicsObject.isPresent()) {
            ctx.log("Gloop");
            // Get the locations of the closest projectile and graphics object
            LocalPoint projectileLocation = closestProjectile.get().getTarget();
            LocalPoint graphicsObjectLocation = closestGraphicsObject.get().getLocation();

            // Compare distances
            int projectileDistance = playerLocation.distanceTo(projectileLocation);
            int graphicsDistance = playerLocation.distanceTo(graphicsObjectLocation);

            if (projectileDistance < graphicsDistance) {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                    ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>Protect From Magic</col>");
                }
            } else {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                    ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>Protect From Missiles</col>");
                }
            }
        } else if (closestProjectile.isPresent()) {
            if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>Protect From Magic</col>");
            }
        } else if (closestGraphicsObject.isPresent()) {
            if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>Protect From Missiles</col>");
            }
        }
    }

    @Override
    public void onChatMessage(ChatMessage msg) {
        if (msg.getMessage() != null) {
            String message = msg.getMessage().toLowerCase();
            if (message.contains("aste vengeance")) {
                Var = false;
            }
        }
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.decode("#e0ad01"));
        g.drawString("Status: ", 5, 300);
        g.setColor(Color.RED);
        if (status == null) {
            Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
            g.setFont(font1);
            g.drawString("Null", 45, 300);
        } else {
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.drawString(status, 45, 300);
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