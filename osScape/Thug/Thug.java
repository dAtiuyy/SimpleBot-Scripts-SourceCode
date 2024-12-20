package Thug;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.queries.SimpleEntityQuery;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.hooks.simplebot.Paintable;

import javax.swing.*;

@ScriptManifest(author = "alex", category = Category.THIEVING, description = "", discord = "",
        name = "Thug", servers = { "osrsps" }, version = "0.1")

public class Thug extends Script {
    public String status;
    public long startTime, currentExp;
    public int count, startExperience;
    @Override
    public void onChatMessage(ChatMessage chatMessage) {
    }

    @Override
    public void onExecute() {
        startTime = System.currentTimeMillis();
        startExperience = ctx.skills.experience(SimpleSkills.Skills.THIEVING);
        currentExp = this.ctx.skills.experience(SimpleSkills.Skills.THIEVING);
    }

    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filter(5730).nextNearest();
        if (currentExp != this.ctx.skills.experience(SimpleSkills.Skills.THIEVING)) {
            count++;
            currentExp = this.ctx.skills.experience(SimpleSkills.Skills.THIEVING);
        }
        if (target != null) {
            target.menuAction("Pickpocket");
        }
    }

    @Override
    public void onTerminate() {}
    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        g.setColor(Color.decode("#e0ad01"));
        g.drawString("Thug    v. " + "0.1", 12, 220);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 242);
        g.drawString("Status: " + status, 14, 256);
        g.drawString("Pickpockets: " + count, 14, 268);
    }
    public void status(final String status) {
        this.status = status;
    }
}
