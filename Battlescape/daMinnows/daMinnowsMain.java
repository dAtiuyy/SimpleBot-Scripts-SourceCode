package daMinnows;

import java.awt.*;

import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;


@ScriptManifest(author = "alex", category = Category.FISHING, description = "*Fucks* fishes and banks em up", discord = "",
        name = "daMinnows", servers = { "Battlescape" }, version = "1.2")

public class daMinnowsMain extends Script {
    public String status;
    public long startTime;
    public int startExperience, fishes, crazy_fishes;
    private static final int FISH_ORE_ID = 7730;


    @Override
    public void onExecute() {
        System.out.println("Started daMinnows");
        this.startExperience = ctx.skills.experience(SimpleSkills.Skills.FISHING);
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {

        if (ctx.inventory.inventoryFull()) {
            SimpleObject bank = ctx.objects.populate().filter("Bank chest").nearest().next();
            if (bank == null) {
                return;
            }
            if (ctx.bank.bankOpen() == false) {
                status("Banking 1");
                bank.click("Bank");
                ctx.sleepCondition(() -> ctx.bank.bankOpen(), 2500);
                return;
            } else if (ctx.inventory.inventoryFull()) {
                status("Banking 2");
                ctx.bank.depositAllExcept(307,13431);
                ctx.sleepCondition(() -> ctx.inventory.inventoryFull() == false, 2500);
                return;
            }
        }

        if (ctx.bank.bankOpen() && !ctx.inventory.inventoryFull()) {
            status("Closing bank");
            ctx.bank.closeBank();
            ctx.sleepCondition(() -> ctx.bank.bankOpen() == false, 2500);
        }

        SimpleNpc fish_ore = ctx.npcs.populate().filter(FISH_ORE_ID).nearest().next();

        if (ctx.players.getLocal().getAnimation() == -1) {
            if (fish_ore != null) {
                fish_ore.click(0);
                status("Fishing");
                ctx.sleepCondition(() -> ctx.players.getLocal().getAnimation() != -1, 500);
            }
        }
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public void onChatMessage(ChatMessage m) {
        if (m.getMessage() != null) {
            String message = m.getMessage().toLowerCase();
            if (message.contains("ou catch ")) {
                fishes++;
            }
            if (message.contains("seller might be interested in this unusual fish")) {
                crazy_fishes++;
            }
        }
    }


    public void status(final String status) {
        this.status = status;
    }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(368, 260, 150, 75);
        g.setColor(Color.BLACK);
        g.drawRect(368, 260, 150, 75);

        Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
        g.setFont(font1);
        g.setColor(Color.GRAY);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
        g.drawString("Status: " + status, 380, 298);
        int totalExp = ctx.skills.experience(SimpleSkills.Skills.FISHING) - startExperience;
        g.drawString("XP: " + ctx.paint.formatValue(totalExp) + " (" + ctx.paint.valuePerHour(totalExp/1000, startTime) + "k)", 380, 308);
        g.drawString("Fishes: " + ctx.paint.formatValue(fishes) + " (" + ctx.paint.valuePerHour(fishes, startTime) + ")", 380, 320);
        g.drawString("Unusuals: " + ctx.paint.formatValue(crazy_fishes) + " (" + ctx.paint.valuePerHour(crazy_fishes, startTime) + ")", 380, 332);
        Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
        g.setFont(font2);
        g.setColor(Color.WHITE);
        g.drawString("daMinnows  v. " + "1.2", 380, 274);
    }
}