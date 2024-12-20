package Levi;

import net.runelite.api.*;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.eventbus.Subscribe;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;

import java.awt.*;
import java.util.*;
import java.util.List;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "", name = "Levi", servers = {
        "osrsps" }, version = "0.1")

public class Levi extends Script implements LoopingScript {
    public String status;
    public boolean Var;
    public int[] PotsIds = {143, 3030, 141, 3028, 139, 3026, 2434, 3024};
    @Override
    public void onExecute() {}

    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filter(12214).nextNearest();
        SimpleItem food = ctx.inventory.populate().filterHasAction("Eat").next();
        SimpleItem pots = ctx.inventory.populate().filter(PotsIds).next();
        SimpleItem runePouch = ctx.inventory.populate().filter(12791).next();


        if (ctx.players.getLocal().getHealth() <= 30 && food != null) {
            status("EAT");
            food.menuAction("Eat");
        }

        if (ctx.prayers.points() <= 20 && pots != null) {
            status("DRINK");
            pots.menuAction("Drink");
        }
        if (runePouch != null && ctx.varpbits.varpbit(4070) == 2) {
            if (ctx.varpbits.varpbit(Varbits.VENGEANCE_COOLDOWN) == 0 && !Var) {
                status("VENG");
                ctx.menuActions.sendAction(57, -1, 14286987, 1, "Cast", "<col=00ff00>Vengeance</col>");
                Var = true;
            }
        }
        if (target != null) {
            if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR) && ctx.prayers.points() > 0) {
                ctx.menuActions.sendAction(57, -1 , 35455009, 1, "Activate", "<col=ff9040>Rigour</col>");
            }
        } else {
            if (ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR)) {
                ctx.menuActions.sendAction(57, -1 , 35455009, 1, "Deactivate", "<col=ff9040>Rigour</col>");
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
    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        List<Projectile> projectiles = ctx.projectiles.getActiveProjectiles();
        Optional<Projectile> closestProjectile = projectiles.stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(Projectile::getRemainingCycles));

        closestProjectile.ifPresent(projectile -> {
            if (ctx.prayers.points() > 0) {
                if (!ctx.npcs.populate().filter(12214).isEmpty()) {
                    if (projectile.getId() == 1662 && !ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                        ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>Protect From Magic</col>");
                    } else if (projectile.getId() == 1663 && !ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                        ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>Protect From Missiles</col>");
                    } else if (projectile.getId() == 1663 && !ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                        ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>Protect From Missiles</col>");
                    }
                } else {
                    if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                        ctx.menuActions.sendAction(57, -1, 35454997, 1, "Deactivate", "<col=ff9040>Protect From Magic</col>");
                    } else if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                        ctx.menuActions.sendAction(57, -1, 35454998, 1, "Deactivate", "<col=ff9040>Protect From Missiles</col>");
                    }
                }
            }
        });
    }

    @Override
    public void onTerminate() {}

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