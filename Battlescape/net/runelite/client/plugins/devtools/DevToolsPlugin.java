package net.runelite.client.plugins.devtools;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.common.collect.ImmutableList;
import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import static java.lang.Math.min;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.CommandExecuted;
import net.runelite.api.events.ExperienceChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.JagexColors;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
	name = "Developer Tools",
	tags = {"panel"},
	developerPlugin = true
)
@Getter
public class DevToolsPlugin extends Plugin
{
	private static final List<MenuAction> EXAMINE_MENU_ACTIONS = ImmutableList.of(MenuAction.EXAMINE_ITEM,
			MenuAction.EXAMINE_ITEM_GROUND, MenuAction.EXAMINE_NPC, MenuAction.EXAMINE_OBJECT);

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private DevToolsOverlay overlay;

	@Inject
	private LocationOverlay locationOverlay;

	@Inject
	private SceneOverlay sceneOverlay;

	@Inject
	private CameraOverlay cameraOverlay;

	@Inject
	private WorldMapLocationOverlay worldMapLocationOverlay;

	@Inject
	private WorldMapRegionOverlay mapRegionOverlay;

	@Inject
	private EventBus eventBus;

	private DevToolsButton players;
	private DevToolsButton npcs;
	private DevToolsButton groundItems;
	private DevToolsButton groundObjects;
	private DevToolsButton gameObjects;
	private DevToolsButton graphicsObjects;
	private DevToolsButton walls;
	private DevToolsButton decorations;
	private DevToolsButton inventory;
	private DevToolsButton projectiles;
	private DevToolsButton location;
	private DevToolsButton chunkBorders;
	private DevToolsButton mapSquares;
	private DevToolsButton validMovement;
	private DevToolsButton lineOfSight;
	private DevToolsButton cameraPosition;
	private DevToolsButton worldMapLocation ;
	private DevToolsButton tileLocation;
	private DevToolsButton interacting;
	private DevToolsButton examine;
	private DevToolsButton detachedCamera;
	private DevToolsButton widgetInspector;
	private DevToolsButton varInspector;
	private NavigationButton navButton;

	@Provides
	DevToolsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DevToolsConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		players = new DevToolsButton("Players");
		npcs = new DevToolsButton("NPCs");

		groundItems = new DevToolsButton("Ground Items");
		groundObjects = new DevToolsButton("Ground Objects");
		gameObjects = new DevToolsButton("Game Objects");
		graphicsObjects = new DevToolsButton("Graphics Objects");
		walls = new DevToolsButton("Walls");
		decorations = new DevToolsButton("Decorations");

		inventory = new DevToolsButton("Inventory");
		projectiles = new DevToolsButton("Projectiles");

		location = new DevToolsButton("Location");
		worldMapLocation = new DevToolsButton("World Map Location");
		tileLocation = new DevToolsButton("Tile Location");
		cameraPosition = new DevToolsButton("Camera Position");

		chunkBorders = new DevToolsButton("Chunk Borders");
		mapSquares = new DevToolsButton("Map Squares");

		lineOfSight = new DevToolsButton("Line Of Sight");
		validMovement = new DevToolsButton("Valid Movement");
		interacting = new DevToolsButton("Interacting");
		examine = new DevToolsButton("Examine");

		detachedCamera = new DevToolsButton("Detached Camera");
		widgetInspector = new DevToolsButton("Widget Inspector");
		varInspector = new DevToolsButton("Var Inspector");

		overlayManager.add(overlay);
		overlayManager.add(locationOverlay);
		overlayManager.add(sceneOverlay);
		overlayManager.add(cameraOverlay);
		overlayManager.add(worldMapLocationOverlay);
		overlayManager.add(mapRegionOverlay);

		final DevToolsPanel panel = injector.getInstance(DevToolsPanel.class);

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "devtools_icon.png");

		navButton = NavigationButton.builder()
			.tooltip("Developer Tools")
			.icon(icon)
			.priority(1)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
		overlayManager.remove(locationOverlay);
		overlayManager.remove(sceneOverlay);
		overlayManager.remove(cameraOverlay);
		overlayManager.remove(worldMapLocationOverlay);
		overlayManager.remove(mapRegionOverlay);
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onCommandExecuted(CommandExecuted commandExecuted)
	{
		String[] args = commandExecuted.getArguments();

		switch (commandExecuted.getCommand())
		{
			case "logger":
			{
				final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
				String message;
				Level currentLoggerLevel = logger.getLevel();

				if (args.length < 1)
				{
					message = "Logger level is currently set to " + currentLoggerLevel;
				}
				else
				{
					Level newLoggerLevel = Level.toLevel(args[0], currentLoggerLevel);
					logger.setLevel(newLoggerLevel);
					message = "Logger level has been set to " + newLoggerLevel;
				}

				client.addChatMessage(ChatMessageType.SERVER, "", message, null);
				break;
			}
			case "getvarp":
			{
				int varp = Integer.parseInt(args[0]);
				int value = client.getVarpValue(client.getVarps(), varp);
				client.addChatMessage(ChatMessageType.SERVER, "", "VarPlayer " + varp + ": " + value, null);
				break;
			}
			case "setvarp":
			{
				int varp = Integer.parseInt(args[0]);
				int value = Integer.parseInt(args[1]);
				client.setVarpValue(client.getVarps(), varp, value);
				client.addChatMessage(ChatMessageType.SERVER, "", "Set VarPlayer " + varp + " to " + value, null);
				VarbitChanged varbitChanged = new VarbitChanged();
				varbitChanged.setIndex(varp);
				eventBus.post(varbitChanged); // fake event
				break;
			}
			case "getvarb":
			{
				int varbit = Integer.parseInt(args[0]);
				int value = client.getVarbitValue(client.getVarps(), varbit);
				client.addChatMessage(ChatMessageType.SERVER, "", "Varbit " + varbit + ": " + value, null);
				break;
			}
			case "setvarb":
			{
				int varbit = Integer.parseInt(args[0]);
				int value = Integer.parseInt(args[1]);
				client.setVarbitValue(client.getVarps(), varbit, value);
				client.addChatMessage(ChatMessageType.SERVER, "", "Set varbit " + varbit + " to " + value, null);
				eventBus.post(new VarbitChanged()); // fake event
				break;
			}
			case "addxp":
			{
				Skill skill = Skill.valueOf(args[0].toUpperCase());
				int xp = Integer.parseInt(args[1]);

				int totalXp = client.getSkillExperience(skill) + xp;
				int level = min(Experience.getLevelForXp(totalXp), 99);

				client.getBoostedSkillLevels()[skill.ordinal()] = level;
				client.getRealSkillLevels()[skill.ordinal()] = level;
				client.getSkillExperiences()[skill.ordinal()] = totalXp;

				client.queueChangedSkill(skill);

				ExperienceChanged experienceChanged = new ExperienceChanged();
				experienceChanged.setSkill(skill);
				eventBus.post(experienceChanged);
				break;
			}
			case "anim":
			{
				int id = Integer.parseInt(args[0]);
				Player localPlayer = client.getLocalPlayer();
				localPlayer.setAnimation(id);
				localPlayer.setActionFrame(0);
				break;
			}
			case "gfx":
			{
				int id = Integer.parseInt(args[0]);
				Player localPlayer = client.getLocalPlayer();
				localPlayer.setGraphic(id);
				localPlayer.setSpotAnimFrame(0);
				break;
			}
			case "transform":
			{
				int id = Integer.parseInt(args[0]);
				Player player = client.getLocalPlayer();
				player.getPlayerComposition().setTransformedNpcId(id);
				player.setIdlePoseAnimation(-1);
				player.setPoseAnimation(-1);
				break;
			}
			case "cape":
			{
				int id = Integer.parseInt(args[0]);
				Player player = client.getLocalPlayer();
				player.getPlayerComposition().getEquipmentIds()[KitType.CAPE.getIndex()] = id + 512;
				player.getPlayerComposition().setHash();
				break;
			}
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (!examine.isActive())
		{
			return;
		}

		MenuAction action = MenuAction.of(event.getType());

		if (EXAMINE_MENU_ACTIONS.contains(action))
		{
			MenuEntry[] entries = client.getMenuEntries();
			MenuEntry entry = entries[entries.length - 1];

			final int identifier = event.getIdentifier();
			String info = "ID: ";

			if (action == MenuAction.EXAMINE_NPC)
			{
				NPC npc = client.getCachedNPCs()[identifier];
				info += npc.getId();
			}
			else
			{
				info += identifier;

				if (action == MenuAction.EXAMINE_OBJECT)
				{
					WorldPoint point = WorldPoint.fromScene(client, entry.getParam0(), entry.getParam1(), client.getPlane());
					info += " X: " + point.getX() + " Y: " + point.getY();
				}
			}

			entry.setTarget(entry.getTarget() + " " + ColorUtil.prependColorTag("(" + info + ")", JagexColors.MENU_TARGET));
			client.setMenuEntries(entries);
		}
	}
}
