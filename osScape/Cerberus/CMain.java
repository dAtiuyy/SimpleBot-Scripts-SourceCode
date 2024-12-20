package Cerberus;

import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayUtil;
import simple.hooks.filters.SimplePrayers;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.LoopingScript;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Set;

@ScriptManifest(author = "Alex", category = Category.FAVORITE, description = "",
        discord = "", name = "Cerberus", servers = {"osrsps"}, version = "0.1")

public class CMain extends Script implements LoopingScript {
    public String status;
    public long startTime;
    public boolean Var;
    WorldArea combatArea;
    ArrayList<WorldPoint> availableTiles;
    private final Set<Integer> excludedObjectIDs = Set.of(21704, 21705,21706, 21707, 21697, 21760, 21698, 21700, 21701, 21749, 20196, 21699, 21708, 21711, 21750, 21751,
            21712, 21709, 21710, 27059, 21702, 21703);
    @Override
    public void onChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getMessage() != null) {
            String message = chatMessage.getMessage().toLowerCase();
            if (message.contains("aste vengeance")) {
                Var = false;
            }
        }
    }

    @Override
    public void onExecute() {
        this.startTime = System.currentTimeMillis();
        WorldPoint Location = ctx.objects.populate().filter(21773).nextNearest().getLocation();
        combatArea = new WorldArea
                (
                        new WorldPoint(Location.getX() - 5, Location.getY() + 11, Location.getPlane()),
                        new WorldPoint(Location.getX() + 7, Location.getY() + 23, Location.getPlane())
                );
        availableTiles = new ArrayList<>();
        availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
    }

    @Override
    public void onProcess() {
        SimpleNpc target = ctx.npcs.populate().filter("Cerberus").nextNearest();
        SimpleItem runePouch = ctx.inventory.populate().filter(12791).next();
        if (runePouch != null && ctx.varpbits.varpbit(4070) == 2) {
            if (ctx.varpbits.varpbit(Varbits.VENGEANCE_COOLDOWN) == 0 && !Var) {
                ctx.menuActions.sendAction(57, -1, 14286987, 1, "Cast", "<col=00ff00>Vengeance</col>");
                Var = true;
            }
        } //veng shit
        if (target != null) {
            //logAllGameObjects(); //Projectile ID: 1247
            if (ctx.prayers.points() > 0) {
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PIETY)) {
                    ctx.menuActions.sendAction(57, -1, 35455011, 1, "Activate", "<col=ff9040>Piety</col>");
                }
                if (!ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MELEE)) {
                    ctx.menuActions.sendAction(57, -1, 35454999, 1, "Activate", "<col=ff9040>Protect from Melee</col>");
                }
            }
        } else {
            if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PIETY)) {
                ctx.menuActions.sendAction(57, -1, 35455011, 1, "Deactivate", "<col=ff9040>Piety</col>");
            }
            if (ctx.prayers.prayerActive(SimplePrayers.Prayers.PROTECT_FROM_MELEE)) {
                ctx.menuActions.sendAction(57, -1, 35454999, 1, "Deactivate", "<col=ff9040>Protect from Melee</col>");
            }
        }
        /*
        if (!ctx.objects.populate().filter("Soul devourer").isEmpty() || !ctx.npcs.populate().filter(9025).isEmpty()) {
            for (SimpleObject badTile : ctx.objects.populate().filter("Soul devourer")) {
                if (availableTiles.contains(badTile.getLocation())) {
                    availableTiles.remove(badTile.getLocation());
                }
            }
            for (SimpleNpc badNpc : ctx.npcs.populate().filter(9025)) {
                if (availableTiles.contains(badNpc.getLocation())) {
                    availableTiles.remove(badNpc.getLocation());
                }
            }
            if (!availableTiles.contains(ctx.players.getLocal().getLocation())) {
                // Find the nearest available tile
                WorldPoint playerLocation = ctx.players.getLocal().getLocation();
                ctx.pathing.step(availableTiles.get(new Random().nextInt(availableTiles.size()-1)));

            }
        } else {
            availableTiles.removeAll(availableTiles);
            availableTiles.addAll(Arrays.asList(combatArea.getWorldPoints()));
        } //safe tile shit square shit
        */
    }
    public void logAllGameObjects() {
        // Stream through all game objects in the current area
        List<GraphicsObject> groundOBJ = ctx.objects.getGraphicsObjects();
        ctx.objects.populate().toStream().forEach(obj -> {
            int id = obj.getId();
            String objName = obj.getName();
            WorldPoint objLocation = obj.getLocation();
            if (NoGo(id)) {
                ctx.log("Object ID: " + id + ", Name: " + objName + ", Location: " + objLocation);
            }
        });
    }
    private boolean NoGo(int id) {
        return !Set.of(20737, 7834, 23112, 1502, 21696, 23106, 21699, 21698, 21708, 7823, 21769, 21946, 21697, 12931, 21780,
                6926, 21777, 85, 23100, 14674, 7825, 732, 7827, 21765, 7824, 7828, 21714, 21702, 22495, 23108, 12932, 21716,
                21763, 21703, 12930, 21706, 21770, 11853, 23610, 21779, 23107, 21705, 21701, 21775, 21761, 21776, 21759, 21766,
                21709, 26294, 17118, 23102, 21751, 7830, 21704, 23109, 21947, 22494, 14675, 7826, 21760, 21700, 21718, 21768,
                26570, 21762, 21767, 21707, 21748, 21711, 14645, 26571, 7829, 20196, 21758, 23104, 21749, 21750, 21752, 21756,
                21712, 27059, 21753, 21773, 23101, 21713, 21772, 21717, 21715, 21757, 21755, 21710, 21754).contains(id);
    }

    @Override
    public void onTerminate() {
    }
    public void status(final String status) {
        this.status = status;
    }
    @Override
    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;
        Player localPlayer = ctx.getClient().getLocalPlayer(); // Get the local player

        // Render tiles in the combat area
        if (combatArea != null) {
            for (WorldPoint pw : combatArea.getWorldPoints()) {
                ctx.paint.drawTileMatrix(g2d, pw, Color.RED);
            }
        }

        // Render ground objects near the player
        //renderGroundObjects(g2d, localPlayer);
    }

    // Method to render nearby ground objects
    private void renderGroundObjects(Graphics2D graphics, Player player) {
        // Get the current plane/level the player is on
        int playerPlane = player.getWorldLocation().getPlane();

        // Iterate through all tiles on the player's current plane
        Tile[][] tiles = ctx.getClient().getScene().getTiles()[playerPlane];

        for (Tile[] tileRow : tiles) {
            for (Tile tile : tileRow) {
                if (tile != null) {
                    GroundObject groundObject = tile.getGroundObject(); // Get the ground object on this tile
                    if (groundObject != null) {
                        // Check if the object's ID is in the exclusion set
                        if (!excludedObjectIDs.contains(groundObject.getId())) {
                            // Use the renderTileObject method to display information about the ground object
                            renderTileObject(graphics, groundObject, player, Color.YELLOW);
                        }
                    }
                }
            }
        }
    }

    // Modified method to render tile objects
    private void renderTileObject(Graphics2D graphics, GroundObject groundObject, Player player, Color color) {
        if (groundObject != null && player.getLocalLocation().distanceTo(groundObject.getLocalLocation()) <= 2400) {
            OverlayUtil.renderTileOverlay(graphics, groundObject, "ID: " + groundObject.getId(), color);
        }
    }
    @Override
    public int loopDuration() {
        return 150;
    }
}