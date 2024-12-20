package Bandos;

import simple.hooks.filters.SimpleGroundItems;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleGroundItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;

import java.awt.*;
import java.util.Objects;

@ScriptManifest(author = "Alex", category = Category.FAVORITE, description = "",
        discord = "", name = "Money", servers = {"osrsps"}, version = "0.1")

public class Bandos extends Script {
    public String status;
    public long startTime;
    public int[] LootIds = {11818, 11820, 11822, 11832, 11834, 11836, 11812};

    @Override
    public void onChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filterContains("General Graardor").nextNearest();
        SimpleGroundItem Loot = ctx.groundItems.populate().filter(LootIds).next();
        if (target == null && Loot == null) {
            if (ctx.combat.getSpecialAttackPercentage() <= 10) {
                if (!ctx.objects.populate().filter(30123).isEmpty()) {
                    ctx.menuActions.sendAction(3, 51, 51, 30123, "Drink", "");
                } else {
                    ctx.menuActions.sendAction(57, -1,14286993, 1, "Cast", "<col=00ff00>Arceuus Home Teleport</col>");
                }
            } else {
                ctx.menuActions.sendAction(3, 53, 47, 30120, "Last-teleport", "");
            }
        } else if (ctx.objects.populate().filter(26503).isEmpty() && ctx.pathing.distanceTo2D(target.getLocation(), ctx.players.getLocal().getLocation()) >= 3) {
            ctx.menuActions.sendAction(5, 55, 50, 26503, "Instance (solo)", "");
        }
        //BULLSHIT gh is a dumbass!!!!!!!!!
        if (Loot != null) {
            Loot.menuAction("Take");
        } else {
            if (target != null) {
                if (ctx.combat.getSpecialAttackPercentage() >= 25) {
                    if (ctx.varpbits.varpbit(301) != 1) {
                        ctx.menuActions.sendAction(57, -1, 10485795, 1, "Use", "<col=ff9040>Special Attack</col>");
                    } else {
                        target.menuAction("Attack");
                    }
                }
            } //attacking LOW PRIORITY
        }//looting HIGH PRIORITY
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.decode("#e0ad01"));
        g.drawString("Status: ", 10, 300);
        g.drawString("Time: ", 10, 320);
        g.setColor(Color.RED);
        Font font1 = new Font("Karla", Font.BOLD, 10); // Adjust the font family, style, and size as desired
        g.setFont(font1);
        g.drawString(Objects.requireNonNullElse(status, "Null"), 50, 300);
        g.drawString(ctx.paint.formatTime(System.currentTimeMillis() - startTime), 45, 320);
    }
}
