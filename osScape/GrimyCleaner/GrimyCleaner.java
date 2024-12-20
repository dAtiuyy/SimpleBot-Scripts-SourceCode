package GrimyCleaner;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import simple.hooks.filters.SimpleBank;
import simple.hooks.filters.SimpleInventory;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "Cleans all of ur grimy herbs", discord = "",
        name = "Grimy Cleaner", servers = { "osrsps" }, version = "1.5")
public class GrimyCleaner extends Script {
    public String status;
    public long startTime, currentExp;
    public int count, startExperience, invCount;
    public String[] stringIDs = {
            "Grimy guam leaf",
            "Grimy marrentill",
            "Grimy tarromin",
            "Grimy harralander",
            "Grimy ranarr weed",
            "Grimy toadflax",
            "Grimy irit leaf",
            "Grimy avantoe",
            "Grimy kwuarm",
            "Grimy snapdragon",
            "Grimy cadantine",
            "Grimy lantadyme",
            "Grimy dwarf weed",
            "Grimy torstol"};
    private static boolean hidePaint = false;

    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
        startExperience = ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        currentExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
    }

    @Override
    public void onProcess() {
        SimpleObject banks = ctx.objects.populate().filterHasAction("Bank").nextNearest();
        SimpleItem Herbs = ctx.inventory.populate().filterContains(stringIDs).reverse().next();
        if (currentExp != this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE)) {
            count++;
            currentExp = this.ctx.skills.experience(SimpleSkills.Skills.HERBLORE);
        }
        if (!ctx.bank.bankOpen()) {
            for (String herbName : stringIDs) {
                status("Cleaning " + herbName);
                if (!ctx.inventory.populate().filter(herbName).isEmpty()) {
                    while (!ctx.inventory.populate().filter(herbName).isEmpty()) {
                        if (Herbs == null) {
                            break;
                        }
                        ctx.inventory.populate().filter(herbName).reverse().next().menuAction("Clean");
                        ctx.sleep(25);
                    }
                }
            }
        }
        if (Herbs != null && ctx.bank.bankOpen()){
            ctx.menuActions.sendAction(57, 11, 786434, 1, "Close", "");
        }
        if (Herbs == null) {
            if (banks == null) {
                return;
            }
            if (!ctx.bank.bankOpen()) {
                status("Banking 1");
                banks.menuAction("Bank");
                ctx.sleepCondition(() -> ctx.bank.bankOpen(), 2500);
            } else {
                if (!ctx.inventory.contains(Herbs)) {
                    ctx.menuActions.sendAction(57, -1, 786474, 1, "Deposit inventory", "");
                }
                ctx.sleepCondition(() -> !ctx.inventory.inventoryFull(), 2500);
                status("Take out");
                for (String herbName : stringIDs) {
                    if (ctx.bank.populate().filterContains(herbName).next() != null) {
                        if (ctx.bank.populate().filterContains(herbName).next().menuAction("Withdraw-All")) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public void onChatMessage(ChatMessage m) {
    }


    public void status(final String status) {
        this.status = status;
    }

    public void paint(Graphics g1) {
        Point mousePos = ctx.mouse.getPoint();
        if (mousePos != null) {
            Rectangle paintRect = new Rectangle(368, 260, 150, 75);
            hidePaint = paintRect.contains(mousePos.getLocation());
        }

        Graphics2D g = (Graphics2D) g1;

        if (!hidePaint){
            g.setColor(Color.DARK_GRAY);
            g.fillRect(368, 260, 150, 75);
            g.setColor(Color.BLACK);
            g.drawRect(368, 260, 150, 75);

            Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
            g.setFont(font1);
            g.setColor(Color.GRAY);
            g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 380, 286);
            g.drawString("Status: " + status, 380, 298);
            g.drawString("Cleaned: " + ctx.paint.formatValue(count), 380, 308);
            g.drawString("invCount: " + invCount, 380, 318);
            Font font2 = new Font("Karla", Font.BOLD, 12); // Adjust the font family, style, and size as desired
            g.setFont(font2);
            g.setColor(Color.WHITE);
            g.drawString("daCleaner  v. " + "2.0", 380, 274);
        }
    }
}
