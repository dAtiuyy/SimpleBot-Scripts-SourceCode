package Gorilla;

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

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "", name = "Gorilla", servers = {
        "osrsps" }, version = "0.1")

public class Gorilla extends Script implements LoopingScript {
    private GorillaGui gui;
    public String status;
    public boolean Var;
    public int[] PotsIds = {143, 3030, 141, 3028, 139, 3026, 2434, 3024};
    public int[] meleeGearIDs = {6570, 19675, 10551, 19722, 13239, 19553};
    public int[] rangeGearIDs = {25867, 21898, 23975, 19930, 19547};
    public String[] actions = {"Wield", "Wear"};
    @Override
    public void onExecute() {
        gui = new GorillaGui();
        gui.show();
    }

    @Override
    public void onProcess() {
        SimpleNpc DEMONIC = ctx.npcs.populate().filter("Demonic gorilla").nextNearest();
        SimpleItem food = ctx.inventory.populate().filterHasAction("Eat").next();
        SimpleItem pots = ctx.inventory.populate().filter(PotsIds).next();
        SimpleItem runePouch = ctx.inventory.populate().filter(12791).next();

        int BOULDER_ANIM = 7228;

        if (ctx.players.getLocal().getHealth() <= 30 && food != null) {
            food.menuAction("Eat");
            status("EAT");
        }

        if (ctx.prayers.points() <= 30 && pots != null) {
            pots.menuAction("Drink");
            status("DRINK");
        }
        if (gui != null && gui.isReturnVariable() && runePouch != null && ctx.varpbits.varpbit(4070) == 2) {
            if (ctx.varpbits.varpbit(Varbits.VENGEANCE_COOLDOWN) == 0 && !Var) {
                ctx.menuActions.sendAction(57, -1, 14286987, 1, "Cast", "<col=00ff00>Vengeance</col>");
                Var = true;
                status("VENG");
            }
        }
        if (DEMONIC != null && DEMONIC.getInteracting() != null) {
            if (DEMONIC.getAnimation() == 7226) {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MELEE) && ctx.prayers.points() > 0) {
                    ctx.menuActions.sendAction(57, -1, 35454999, 1, "Activate", "<col=ff9040>Protect from Melee</col>");
                    status("Melee praying");
                }//pray melee
            }
            /*
            if (DEMONIC.getAnimation() == BOULDER_ANIM) { //856 projectile id
                WorldPoint playerLocation = ctx.players.getLocal().getLocation();
                WorldPoint targetLocation = DEMONIC.getLocation();
                if (playerLocation != targetLocation) {
                    ctx.pathing.step(targetLocation);
                }

            }
             */
            if (DEMONIC.getOverhead().equals(HeadIcon.MELEE)) {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR) && ctx.prayers.points() > 0) {
                    ctx.menuActions.sendAction(57, -1, 35455009, 1, "Activate", "<col=ff9040>Eagle Eye</col>");
                }
                for (int gearID : rangeGearIDs) {
                    SimpleItem rangeGear = ctx.inventory.populate().filter(gearID).next();
                    if (rangeGear != null) {
                        for (String action : actions) {
                            if (rangeGear.menuAction(action)) {
                                break; // Exit loop after the first successful action
                            }
                        }
                    }
                }
                status("Head = Melee");
            } else {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PIETY) && ctx.prayers.points() > 0) {
                    ctx.menuActions.sendAction(57, -1, 35455011, 1, "Activate", "<col=ff9040>Piety</col>");
                }
                for (int gearID2 : meleeGearIDs) {
                    SimpleItem meleeGear = ctx.inventory.populate().filter(gearID2).next();
                    if (meleeGear != null) {
                        for (String action : actions) {
                            if (meleeGear.menuAction(action)) {
                                break;
                            }
                        }
                    }
                }
                status("Head = Range");
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
                if (projectile.getId() == 1304 && !ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                    ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>Protect From Magic</col>");
                } else if (projectile.getId() == 1302 && !ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                    ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>Protect From Missiles</col>");
                }
            }
        });
    }

    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.close();
        }
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