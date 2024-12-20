package ATest;

import net.runelite.api.MenuAction;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayUtil;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.interaction.menuactions.SimpleMenuActionType;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleGroundItem;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ScriptManifest(author = "alex", category = Category.FAVORITE, description = "", discord = "",
        name = "A", servers = { "osrsps" }, version = "0.1")
public class ATestMain extends Script implements LoopingScript {
    WorldArea combatArea;
    ArrayList<WorldPoint> availableTiles;
    public int[] ID = {34562, 34563, 34564};

    @Override
    public void onExecute() {
        WorldPoint anchor = ctx.objects.populate().filter(34586).nextNearest().getLocation();
        combatArea = new WorldArea(
                new WorldPoint(anchor.getX() + 5, anchor.getY() - 12, anchor.getPlane()),
                new WorldPoint(anchor.getX() + 26, anchor.getY() + 10, anchor.getPlane())
        );
        availableTiles = new ArrayList<>();
        availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
        if (!ctx.objects.populate().filter(ID).isEmpty()) {
            for (SimpleObject obj : ctx.objects.populate().filter(ID)) {
                WorldPoint objectLocation = obj.getLocation();

                List<WorldPoint> occupiedTiles = new ArrayList<>();
                occupiedTiles.add(objectLocation);
                occupiedTiles.add(new WorldPoint(objectLocation.getX() + 1, objectLocation.getY(), objectLocation.getPlane()));
                occupiedTiles.add(new WorldPoint(objectLocation.getX(), objectLocation.getY() + 1, objectLocation.getPlane()));
                occupiedTiles.add(new WorldPoint(objectLocation.getX() + 1, objectLocation.getY() + 1, objectLocation.getPlane()));

                availableTiles.removeAll(occupiedTiles);
            }
        } else {
            availableTiles.removeAll(availableTiles);
            availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
        }
    }



    @Override
    public void onProcess() {
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        if (!ctx.objects.populate().filter(ID).isEmpty()) {
            for (SimpleObject obj : ctx.objects.populate().filter(ID)) {
                if (ctx.players.getLocal().getLocation().distanceTo(obj.getLocation()) <= 300) {
                    switch (obj.getId()) {
                        case 34562:
                            OverlayUtil.renderTileOverlay((Graphics2D) graphics, obj.getTileObject(),obj.getLocation().toString(), Color.BLUE);
                            break;
                        case 34563:
                            OverlayUtil.renderTileOverlay((Graphics2D) graphics, obj.getTileObject(),obj.getLocation().toString(), Color.GREEN);
                            break;
                        case 34564:
                            OverlayUtil.renderTileOverlay((Graphics2D) graphics, obj.getTileObject(),obj.getLocation().toString(), Color.RED);
                    }
                }
            }
        }
        if (combatArea != null) {
            for (WorldPoint pw : combatArea.getWorldPoints()) {
                //ctx.paint.drawTileMatrix(g2d, pw, Color.RED);
            }
        }
        if (availableTiles != null && !availableTiles.isEmpty()) {
            for (WorldPoint wp : availableTiles) {
                //ctx.paint.drawTileMatrix(g2d, wp, Color.GREEN);
            }//ctx.objects.populate().filter(toolObjIDs).nextNearest().getTileObject().getLocalLocation();
        }
    }
    @Override
    public void onChatMessage(ChatMessage chatMessage) {}

    @Override
    public int loopDuration() {
        return 100;
    }
}
