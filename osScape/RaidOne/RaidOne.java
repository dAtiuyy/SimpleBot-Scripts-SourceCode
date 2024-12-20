package RaidOne;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "", name = "Raid One", servers = {
        "osrsps" }, version = "0.1")

public class RaidOne extends Script implements LoopingScript, KeyListener {
    public String status;
    public int[] meleeGearIDs = {12500, 12502};
    public int[] rangeGearIDs = {23975, 23979};
    public int[] mageGearIDs = {4712, 4714};
    public String[] actions = {"Wield", "Wear"};
    public boolean mageGearVar, rangeGearVar, meleeGearVar;
    public long startTime;
    String[] stringIDs = {
            String.valueOf(Pattern.compile("Prayer potion\\(\\d+\\)")),
            String.valueOf(Pattern.compile("Super restore\\(\\d+\\)"))};

    private boolean key1Pressed = false;
    private boolean key2Pressed = false;
    private boolean key3Pressed = false;

    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }
    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        List<Projectile> projectiles = ctx.projectiles.getActiveProjectiles();
        Optional<Projectile> closestProjectile = projectiles.stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(Projectile::getRemainingCycles));

        closestProjectile.ifPresent(projectile -> {
            if (ctx.prayers.points() > 0) {
                if (projectile.getId() == 1339 || projectile.getId() == 1341) {
                    if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                        ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>Protect From Magic</col>");
                    }
                } else if (projectile.getId() == 1340 || projectile.getId() == 1343) {
                    if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                        ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>Protect from Missiles</col>");
                    }
                } else if (projectile.getId() == 1345) {
                    if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MELEE)) {
                        ctx.menuActions.sendAction(57, -1, 35454999, 1, "Activate", "<col=ff9040>Protect from Melee</col>");
                    }
                }
            }
        });
    }

    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
        ctx.getClient().getCanvas().addKeyListener(this);
    }

    @Override
    public void onProcess() {
        SimpleItem food = ctx.inventory.populate().filterHasAction("Eat").next();
        SimpleItem pots = ctx.inventory.populate().filter(stringIDs).next();
        SimpleNpc rightHand = ctx.npcs.populate().filterContains("Right Claw").nextNearest();
        SimpleNpc leftHand = ctx.npcs.populate().filterContains("Left Claw").nextNearest();
        SimpleNpc olmHead = ctx.npcs.populate().filter("Great Olm").nextNearest();

        if (ctx.players.getLocal().getHealth() <= 30 && food != null) {
            food.menuAction("Eat");
            status("EAT");
        }

        if (ctx.prayers.points() <= 30 && pots != null) {
            pots.menuAction("Drink");
            status("DRINK");
        }
        if (key1Pressed) {
            rangeGearVar = !rangeGearVar;
            equipGear(rangeGearIDs);
            ctx.log("Range Gear Toggled");
            key1Pressed = false;
        }
        if (key2Pressed) {
            mageGearVar = !mageGearVar;
            equipGear(mageGearIDs);
            ctx.log("Mage Gear Toggled");
            key2Pressed = false;
        }
        if (key3Pressed) {
            meleeGearVar = !meleeGearVar;
            equipGear(meleeGearIDs);
            ctx.log("Melee Gear Toggled");
            key3Pressed = false;
        }
    }

    private void equipGear(int[] gearIDs) {
        for (int gearID : gearIDs) {
            SimpleItem gearItem = ctx.inventory.populate().filter(gearID).next();
            if (gearItem != null) {
                for (String action : actions) {
                    if (gearItem.menuAction(action)) {
                        break; // Exit loop after the first successful action
                    }
                }
            }
        }
    }

    @Override
    public void onTerminate() {
        ctx.getClient().getCanvas().removeKeyListener(this);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.decode("#e0ad01"));
        g.drawString("Status: ", 5, 300);
        g.setColor(Color.RED);
        Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
        g.setFont(font1);
        g.drawString(Objects.requireNonNullElse(status, "Null"), 50, 300);
        g.drawString(ctx.paint.formatTime(System.currentTimeMillis() - startTime), 45, 320);
    }

    @Override
    public int loopDuration() {
        return 100;
    }

    public void status(final String status) {
        this.status = status;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_1:
                key1Pressed = true;
                key2Pressed = false;
                key3Pressed = false;
                break;
            case KeyEvent.VK_2:
                key2Pressed = true;
                key1Pressed = false;
                key3Pressed = false;
                break;
            case KeyEvent.VK_3:
                key3Pressed = true;
                key1Pressed = false;
                key2Pressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
