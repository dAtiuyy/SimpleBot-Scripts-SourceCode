package Test;

import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.regex.Pattern;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.hooks.wrappers.SimpleWidget;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

@ScriptManifest(author = "alex", category = Category.MONEYMAKING, description = "", discord = "",
        name = "zomeguy", servers = { "Battlescape" }, version = "1.0")

public class zomeguy extends Script {
    public String status;
    public long startTime;
    public void status(final String status) {this.status = status; } //use this to display wtf ur script is doing atm

    @Override
    public void onProcess() {
        status("baby steps mf"); // this is how you use the status thingy
        WorldPoint currentLocation = ctx.players.getLocal().getLocation();
        SimpleObject Door = ctx.objects.populate().filter(37340).nextNearest(); // no more BULLSHIT!
        if (Door != null && Door.validateInteractable()) {
            Door.click(0); // 0 usually is a simple click
            // its also possible to do .click("string action")
        }
    }

    @Override
    public void onChatMessage(ChatMessage chatMessage) {

    }

    @Override
    public void onExecute() {
        System.out.println("Started le Script dude");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        g.setColor(Color.BLACK);
        g.fillRect(5, 2, 192, 58);
        g.setColor(Color.decode("#ea411c"));
        g.drawRect(5, 2, 192, 58);
        g.drawLine(8, 24, 194, 24);

        g.setColor(Color.decode("#e0ad01"));
        g.drawString("zomeguy                             v. " + "0.1", 12, 20);
        g.drawString("Time: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 14, 42);
        g.drawString("Status: " + status, 14, 56);
    }
}
