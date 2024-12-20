package Bullshit;

import net.runelite.api.Projectile;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.eventbus.Subscribe;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

@ScriptManifest(author = "Alex", category = Category.FAVORITE, description = "",
        discord = "", name = "Boom", servers = {"osrsps"}, version = "0.1")

public class Bullshit extends Script implements LoopingScript {
    private BullshitGui gui;
    public String status;
    public long startTime;
    private Set<Integer> oldProj = new HashSet<>();
    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }

    @Override
    public void onExecute() {
        this.startTime = System.currentTimeMillis();
        gui = new BullshitGui();
        gui.show();
    }

    @Override
    public void onProcess() {
        if (gui != null && gui.isReturnVariable()) {
            projListToLog();
        }

    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        ctx.projectiles.getActiveProjectiles().stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(Projectile::getRemainingCycles))
                .ifPresent(projectile -> {
                    int id = projectile.getId();
                    if (isRangeProjectile(id)) {
                        activatePrayer(SimplePrayers.Prayers.PROTECT_FROM_MISSILES, "Praying Range");
                    } else if (isMageProjectile(id)) {
                        activatePrayer(SimplePrayers.Prayers.PROTECT_FROM_MAGIC, "Praying Mage");
                    } else if (isMeleeProjectile(id)) {
                        activatePrayer(SimplePrayers.Prayers.PROTECT_FROM_MELEE, "Praying Melee");
                    }
                });
    }

    private boolean isRangeProjectile(int id) {
        return Set.of(1044, 124, 475, 1340 ,1343, 1302, 473, 1663).contains(id);
    }

    private boolean isMageProjectile(int id) {
        return Set.of(1046, 1048, 160, 162, 100, 1339, 1341, 448, 1304, 280, 1662, 1735).contains(id);
    }

    private boolean isMeleeProjectile(int id) {
        return Set.of(1248, 1345).contains(id);
    }

    private void activatePrayer(SimplePrayers.Prayers prayer, String statusMessage) {
        if (!ctx.prayers.prayerActive(prayer)) {
            status(statusMessage);
            ctx.menuActions.sendAction(57, -1, getPrayerActionId(prayer), 1, "Activate", "<col=ff9040>" + statusMessage + "</col>");
        }
    }

    private int getPrayerActionId(SimplePrayers.Prayers prayer) {
        switch (prayer) {
            case PROTECT_FROM_MAGIC:
                return 35454997;
            case PROTECT_FROM_MISSILES:
                return 35454998;
            case PROTECT_FROM_MELEE:
                return 35454999;
            default:
                throw new IllegalArgumentException("Unknown prayer: " + prayer);
        }
    }
    public void projListToLog() {
        List<Projectile> projectiles = ctx.projectiles.getActiveProjectiles();
        Set<Integer> nowProj = new HashSet<>();
        for (Projectile proj : projectiles) {
            int projID = proj.getId();
            nowProj.add(projID);
        }
        if (!nowProj.equals(oldProj)) {
            for (int projID : nowProj) {
                ctx.log("Projectile ID: " + projID);
            }
            oldProj = nowProj;
        }
    }

    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.close();
        }
    }
    public void status(final String status) {
        this.status = status;
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.drawString("Status: " + status, 380, 298);
    }
    @Override
    public int loopDuration() {
        return 150;
    }
}