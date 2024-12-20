package Gauntlet;

import net.runelite.api.Projectile;
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

@ScriptManifest(
        servers = {"OldschoolRSPS"},
        discord = "le_keyboard",
        version = "1.1.0",
        name = "Gauntlet",
        author = "Alex",
        category = Category.MINIGAMES,
        description = "<html>"
                + "<p><strong>Gauntlet Helper</strong>  Really bad Gauntlet Helper</p>"
                + "<p><strong>Guidelines & Features:</strong></p>"
                + "<ul>"
                + "<li><strong>Please select the prayer and what style you would like it to use against each overhead.</strong> No saving the settings suckers.</li>"
                + "<li>Doesnt matter where you start it.</li>"
                + "<li>Supports all the styles and weapon tiers.</li>"
                + "<li>This switches prayers and the equipment.</li>"
                + "<li>You have to attack the boss.</li>"
                + "<li>Can be used for both, normal and corrupted gauntlet.</li>"
                + "<li><strong>THIS DOES NOT EAT FOR YOU!!!</strong> If you want it to do that, pay one of our talented Script Writers to make one which does.</li>"
                + "<li><strong>THIS DOES NOT AVOID THE TORNADOES FOR YOU!!!</strong> If you want it to do that, pay one of our talented Script Writers to make one which does.</li>"
                + "</ul>"
                + "</html>"
)
public class Gauntlet extends Script implements LoopingScript {
    private GauntletGui gui;
    public int[] targetIDs = {9021, 9022, 9023, 9035, 9036, 9037};
    private SimplePrayers.Prayers lastPrayer;

    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }

    @Override
    public void onExecute() {
        gui = new GauntletGui();
        gui.show();
    }

    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filter(targetIDs).nextNearest();
        if (gui != null && gui.isScriptStarted()) {
            if (target != null) {
                if (target.getOverhead() != null) {
                    handleTarget(target);
                }
                if (ctx.players.getLocal().getHealth() < gui.FromHealthBox()) {
                    ctx.inventory.populate().filterHasAction("Eat").next().menuAction("Eat");
                }
                if (ctx.prayers.points() < gui.FromPrayerBox()) {
                    ctx.inventory.populate().filterHasAction("Drink").next().menuAction("Drink");
                }
            }
        }
    }

    private void handleTarget(SimpleNpc target) {
        switch (target.getOverhead()) {
            case MELEE:
                switch (gui.FromComboBox3()) {
                    case "Halberd":
                        activatePrayer(gui.FromComboBox6());
                        break;
                    case "Bow":
                        activatePrayer(gui.FromComboBox1());
                        break;
                    case "Staff":
                        activatePrayer(gui.FromComboBox2());
                        break;
                }
                equipGear(ctx.inventory.populate().filterContains(gui.FromComboBox3()).next());
                break;
            case RANGED:
                switch (gui.fromRangeOverhead()) {
                    case "Halberd":
                        activatePrayer(gui.FromComboBox6());
                        break;
                    case "Bow":
                        activatePrayer(gui.FromComboBox1());
                        break;
                    case "Staff":
                        activatePrayer(gui.FromComboBox2());
                        break;
                }
                equipGear(ctx.inventory.populate().filterContains(gui.fromRangeOverhead()).next());
                break;
            case MAGIC:
                switch (gui.FromComboBox5()) {
                    case "Halberd":
                        activatePrayer(gui.FromComboBox6());
                        break;
                    case "Bow":
                        activatePrayer(gui.FromComboBox1());
                        break;
                    case "Staff":
                        activatePrayer(gui.FromComboBox2());
                        break;
                }
                equipGear(ctx.inventory.populate().filterContains(gui.FromComboBox5()).next());
                break;
        }
    }

    private void equipGear(SimpleItem item) {
        if (item != null) {
            item.menuAction("Wield");
        }
    }

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        ctx.projectiles.getActiveProjectiles().stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(Projectile::getRemainingCycles))
                .ifPresent(this::handleProjectile);
    }

    private void handleProjectile(Projectile projectile) {
        int id = projectile.getId();
        if (isRangeProjectile(id)) {
            lastPrayer = SimplePrayers.Prayers.PROTECT_FROM_MISSILES;
            activatePrayer(SimplePrayers.Prayers.PROTECT_FROM_MISSILES);
        } else if (isMageProjectile(id)) {
            lastPrayer = SimplePrayers.Prayers.PROTECT_FROM_MAGIC;
            activatePrayer(SimplePrayers.Prayers.PROTECT_FROM_MAGIC);
        } else if (id == 1714) {
            activatePrayer(lastPrayer);
        }
    }

    private boolean isRangeProjectile(int id) {
        return Set.of(1711, 1712).contains(id);
    }

    private boolean isMageProjectile(int id) {
        return Set.of(1707, 1708).contains(id);
    }

    public static String capitalizeWords(String str) {
        String[] words = str.split(" ");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    private void activatePrayer(SimplePrayers.Prayers prayer) {
        switch (prayer) {
            case PROTECT_FROM_MAGIC:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35454997, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase().replace("_", " ")) + "</col>");
                }
                break;
            case PROTECT_FROM_MISSILES:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35454998, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase().replace("_", " ")) + "</col>");
                }
                break;
            case RIGOUR:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35455009, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase()) + "</col>");
                }
                break;
            case AUGURY:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35455012, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase()) + "</col>");
                }
                break;
            case EAGLE_EYE:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35455005, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase().replace("_", " ")) + "</col>");
                }
                break;
            case MYSTIC_MIGHT:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35455008, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase().replace("_", " ")) + "</col>");
                }
                break;
            case PIETY:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35455011, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase()) + "</col>");
                }
                break;
            case CHIVALRY:
                if (!ctx.prayers.prayerActive(prayer)) {
                    ctx.menuActions.sendAction(57, -1, 35455010, 1, "Activate", "<col=ff9040>" + capitalizeWords(prayer.toString().toLowerCase()) + "</col>");
                }
                break;
        }
    }

    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.close();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
    }

    @Override
    public int loopDuration() {
        return 150;
    }
}
