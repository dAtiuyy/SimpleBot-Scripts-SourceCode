package Tempoross;

import java.awt.*;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;

@ScriptManifest(author = "Limb", category = Category.MINIGAMES, description = "Tempoross",
        discord = "limbabstracter", name = "Tempoross", servers = {"osrsps"}, version = "0.1")

public class Tempoross extends Script {
    public String status;
    public long startTime,lastActionTime;
    public int WAIT_TIME_MS = 1350;
    public int startExperience, fishObtained, fishCookedNumbr;
    private static boolean hidePaint = false;
    private TemporossGui gui;
    @Override
    public void onExecute() {
        this.startTime = System.currentTimeMillis();
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.FISHING);
        gui = new TemporossGui();
        gui.show();
    }
    public void status(final String status) {
        this.status = status;
    }
    @Override
    public void onProcess() {
        SimpleItem harpoon = ctx.inventory.populate().filter(311).next();
        SimpleObject HarpoonBox = ctx.objects.populate().filter(40967).nextNearest();
        SimpleNpc fishSpot = ctx.npcs.populate().filter("Fishing spot").nextNearest();
        SimpleObject climb = ctx.objects.populate().filter("Rope ladder").next();
        SimpleObject cook = ctx.objects.populate().filter("Shrine").nextNearest();
        SimpleItem fish = ctx.inventory.populate().filter(25564).next();
        SimpleNpc fishBucket = ctx.npcs.populate().filter(10576).next();
        SimpleItem fishCooked = ctx.inventory.populate().filter(25565).next();
        SimpleNpc important = ctx.npcs.populate().filter(10571).nextNearest();
        SimpleNpc Fire = ctx.npcs.populate().filter(8643).next(); //41000
        SimpleObject WaterPump = ctx.objects.populate().filter(41000).nextNearest();
        long currentTime = System.currentTimeMillis();
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS) {
            if (harpoon == null){
                HarpoonBox.menuAction("Take");
                status("taking harpoon");
            }
        } else ctx.sleep(500);
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS) {
            if (Fire != null && ctx.inventory.populate().filter("Bucket of water").next() != null) {
                Fire.menuAction("Douse");
                status("Fire bullshit");
            }
        } else ctx.sleep(500);
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS) {
            if (fishBucket != null && fish == null) {
                fishBucket.menuAction("Fill");
                status("Filling");
            }
        } else ctx.sleep(500);
        if (harpoon != null) {
            if (climb != null && ctx.inventory.populate().filter(1925).population() == 3) {
                climb.menuAction("Climb (solo)");
                status("climbing");
            } else ctx.sleep(500);
            if (ctx.inventory.populate().filter(1925).population() == 3 && ctx.inventory.populate().filter("Bucket of water").next() == null) {
                if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS) {
                    if (WaterPump != null && fishSpot != null) {
                        WaterPump.menuAction("Use");
                        status("Water pump");
                    }
                } else ctx.sleep(500);
            }
            if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS && important == null && ctx.inventory.populate().filter("Bucket of water").next() != null) {
                if (fishSpot != null && !ctx.inventory.inventoryFull()) {
                    fishSpot.menuAction("Harpoon");
                    status("fishing");
                }
            } else ctx.sleep(500);
        }
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS) {
            if (cook != null && ctx.inventory.populate().filter(25564).population() >= 24) {
                cook.menuAction("Cook-at");
                status("Cooking");
            }
        } else ctx.sleep(500);
        if (ctx.players.getLocal().getAnimation() == -1 && (currentTime - lastActionTime) > WAIT_TIME_MS && ctx.inventory.contains(harpoon)) {
            if (important != null && !ctx.inventory.contains(fishCooked)) {
                important.menuAction("Harpoon");
                status("IMPORTANT");
            }
        } else ctx.sleep(500);
    }
    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.close();
        }
    }
    @Override
    public void onChatMessage(ChatMessage arg0) {
        if (arg0.getMessage() != null) {
            String message = arg0.getMessage().toLowerCase();
            if (message.contains("ou catch a")) {
                fishObtained++;
            }
            if (message.contains("uo succesfully cook a")) {
                fishCookedNumbr++;
            }
        }
    }
    @Override
    public void paint(Graphics arg0) {
        Point mousePos = ctx.mouse.getPoint();
        if (mousePos != null) {
            Rectangle paintRect = new Rectangle(5, 2, 192, 58);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }
        Graphics2D g = (Graphics2D) arg0;
        if (!hidePaint) {
            g.setColor(Color.BLACK);
            g.fillRect(5, 2, 192, 58);
            g.setColor(Color.decode("#ea411c"));
            g.drawRect(5, 2, 192, 58);
            g.drawLine(8, 24, 194, 24);
            g.setColor(Color.decode("#e0ad01"));
            g.drawString("Temporass                             v. " + "0.1", 12, 20);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 42);
            g.drawString("Status: " + status, 14, 56);
        }
    }
}