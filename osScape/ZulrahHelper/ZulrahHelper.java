package ZulrahHelper;

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
import java.util.Comparator;


@ScriptManifest(author = "Alex", category = Category.FAVORITE, description = "",
        discord = "", name = "Zulrah Helper", servers = {"osrsps"}, version = "0.1")

public class ZulrahHelper extends Script implements LoopingScript {
    private ZulrahHelperGui gui;
    public String status;
    public long startTime;
    public String[] actions = {"Wield", "Wear"};
    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }

    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
        gui = new ZulrahHelperGui();
        gui.show();
    }

    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filterContains("Zulrah").nextNearest();
        if (gui != null && gui.isScriptStarted()) {
            if (target != null) {
                if (ctx.players.getLocal().getHealth() < gui.FromHealthBox()) {
                    ctx.inventory.populate().filterHasAction("Eat").next().menuAction("Eat");
                }
                if (ctx.prayers.points() < gui.FromPrayerBox()) {
                    ctx.inventory.populate().filterHasAction("Drink").next().menuAction("Drink");
                }
                handleTarget(target);
            } else {
                if (ctx.prayers.prayerActive(SimplePrayers.Prayers.RIGOUR)) {
                    ctx.menuActions.sendAction(57, -1 , 35455009, 1, "Deactivate", "<col=ff9040>Rigour</col>");
                }
                if (ctx.prayers.prayerActive(SimplePrayers.Prayers.AUGURY)) {
                    ctx.menuActions.sendAction(57, -1 , 35455012, 1, "Deactivate", "<col=ff9040>Rigour</col>");
                }
                if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MAGIC)) {
                    ctx.menuActions.sendAction(57, -1, 35454997, 1, "Deactivate", "<col=ff9040>Protect From Magic</col>");
                }
                if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MISSILES)) {
                    ctx.menuActions.sendAction(57, -1, 35454998, 1, "Deactivate", "<col=ff9040>Protect From Missiles</col>");
                }
            }
        }


    }
    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.close();
        }
    }
    private void equipGear(SimpleItem item) {
        if (item != null) {
            for (String action : actions) {
                if (item.menuAction(action)) {
                    break;
                }
            }
        }
    }
    private void handleTarget(SimpleNpc target) {
        String[] rangeGear = gui.getRangeGearInput();
        String[] mageGear = gui.getMageGearInput();
        switch (target.getId()) {
            case 2042: // GREEN GUY
            case 2044: // CYAN GUY // TANZENITE
                activatePrayer(gui.FromComboBox1());
                for (String gearID : rangeGear) {
                    SimpleItem Gear = ctx.inventory.populate().filterContains(gearID).next();
                    if (Gear != null) {
                        equipGear(Gear);
                    }
                }
                break;
            case 2043: // RED GUY
                activatePrayer(gui.FromComboBox2());
                for (String gearID : mageGear) {
                    SimpleItem Gear = ctx.inventory.populate().filterContains(gearID).next();
                    if (Gear != null) {
                        equipGear(Gear);
                    }
                }
                break;
        }
    }
    public void status(final String status) {this.status = status;}

    @Subscribe
    public void onProjectileMoved(ProjectileMoved event) {
        ctx.projectiles.getActiveProjectiles().stream()
                .filter(p -> p.getInteracting() != null && p.getInteracting().equals(ctx.players.getLocal().getPlayer()))
                .min(Comparator.comparingInt(Projectile::getRemainingCycles))
                .ifPresent(this::handleProjectile);
    }

    private void handleProjectile(Projectile projectile) {
        int id = projectile.getId();
        if (id == 1044) {
            activatePrayer(SimplePrayers.Prayers.PROTECT_FROM_MISSILES);
        } else if (id == 1046) {
            activatePrayer(SimplePrayers.Prayers.PROTECT_FROM_MAGIC);
        }
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
        }
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

    @Override
    public void paint(Graphics graphics) {
    }
    @Override
    public int loopDuration() {
        return 150;
    }
}