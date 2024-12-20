package Vorkath;

import net.runelite.api.Projectile;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.eventbus.Subscribe;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.interaction.menuactions.SimpleMenuActionType;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;

import java.awt.*;
import java.util.*;

@ScriptManifest(author = "Alex", category = Category.FAVORITE, description = "",
        discord = "", name = "Vorkath", servers = {"osrsps"}, version = "0.1")

public class VorkathMain extends Script implements LoopingScript {
    public String status;
    public long startTime;
    private static boolean hidePaint = false;
    private final Set<Integer> numberS = Set.of(393, 1477, 1470, 1479, 1471);
    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }
    @Override
    public void onExecute() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        SimpleItem runePouch = ctx.inventory.populate().filter(12791).next();
        SimpleNpc undead = ctx.npcs.populate().filter("Zombified Spawn").nextNearest();
        SimpleNpc target = ctx.npcs.populate().filter(8061).nextNearest();
        if (target != null) {
            if (ctx.prayers.points() > 0) {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR)) {
                    status("Eagle Eye ON");
                    ctx.menuActions.sendAction(57, -1, 35455009, 1, "Activate", "<col=ff9040>Eagle Eye</col>");
                }
            }
        } else {
            if (ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR)) {
                status("Rigour OFF");
                ctx.menuActions.sendAction(57, -1, 35455009, 1, "Deactivate", "<col=ff9040>Eagle Eye</col>");
            }
        }
        if (undead != null && runePouch != null) {
            status("Crumble Undead");
            ctx.menuActions.sendAction(25, -1, 14286876, 0, "Cast", "<col=00ff00>Crumble Undead</col>");
            ctx.sleep(25, 75);
            undead.menuAction(SimpleMenuActionType.SPELL_CAST_ON_NPC);
        }
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        ctx.projectiles.getActiveProjectiles().stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(Projectile::getRemainingCycles))
                .ifPresent(projectile -> {
                    int id = projectile.getId();
                    if (ctx.prayers.points() > 0) {
                        if (numberS.contains(id)) {
                            if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                                status("Switching Prayers");
                                ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>Protect from Magic</col>");
                            }
                        }
                    }
                });
    }
    @Override
    public void onTerminate() {
    }
    public void status(final String status) {
        this.status = status;
    }

    @Override
    public void paint(Graphics graphics) {
        Point mousePos = ctx.mouse.getPoint();
        if (mousePos != null) {
            Rectangle paintRect = new Rectangle(5, 212, 192, 58);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }
        Graphics2D g = (Graphics2D) graphics;
        if (!hidePaint) {
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 242);
            g.drawString("Status: " + status, 14, 256);
        }
    }
    @Override
    public int loopDuration() {
        return 150;
    }
}