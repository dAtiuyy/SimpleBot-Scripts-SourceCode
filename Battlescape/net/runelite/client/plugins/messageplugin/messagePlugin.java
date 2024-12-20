package net.runelite.client.plugins.messageplugin;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.events.GameTick;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;

@PluginDescriptor(
        name = "le message plugin",
        description = "Automatically attacks NPCs and banks, eats food, and uses potions",
        tags = {"@nukeafrica", "@autoattackerv2"},
        enabledByDefault = false
)

@Slf4j
public class messagePlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private net.runelite.client.plugins.messageplugin.AutoAttackerXConfig config;

    static final String CONFIG_GROUP = "autoattackerX";
    private State currentState;
    private State previousState;
    private State nextState;

    private WorldPoint itemDropLocation;
    private WorldPoint savedLocation;

    public enum State
    {
        ATTACKING(false),
        LOOTING(false),
        BANKING(false),
        EATING(false),
        BURYING(false),
        USING_POTIONS(false),
        OPENING_BANK(false),
        NAVIGATING_TO_BANK(false),
        NAVIGATING_BACK_FROM_BANK(false),
        NAVIGATING_TO_ITEM(false),
        NAVIGATING_TO_NPC(false),
        IDLING(false),
        LOGGING_IN(false),
        LOGGED_OUT(false);

        private boolean hasEntered;
        private boolean isComplete;

        State(boolean hasEntered) {
            this.hasEntered = hasEntered;
            this.isComplete = false;
        }

        public boolean hasEntered() {
            return hasEntered;
        }

        public boolean isComplete() {
            return isComplete;
        }

        public void setIsComplete(boolean isComplete) {
            this.isComplete = isComplete;
        }

        public void setHasEntered(boolean hasEntered) {
            this.hasEntered = hasEntered;
        }
    }

    @Provides
    AutoAttackerXConfig getConfig(ConfigManager configManager)
    {
        return configManager.getConfig(AutoAttackerXConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        log.info("atv2: Plugin started");
        previousState = State.IDLING;
        currentState = State.IDLING;
        nextState = State.IDLING;
        itemDropLocation = null;
        savedLocation = null;
    }

    @Override
    protected void shutDown() throws Exception
    {
        log.info("atv2: Plugin stopped");
        changeState(State.IDLING);
        itemDropLocation = null;
        savedLocation = null;
    }

    public void changeState(State newState)
    {
        previousState = currentState;
        currentState = newState;
        nextState = newState;
        currentState.setHasEntered(false);
        currentState.setIsComplete(false);
    }

    public ArrayList<String> getLootList()
    {
        ArrayList<String> lootArray = new ArrayList<String>(100);
        String[] lootList = config.lootList().split(",");
        for (String loot : lootList)
        {
            lootArray.add(loot.toLowerCase());
        }
        return lootArray;
    }

    public ArrayList<String> getDepositBlacklist()
    {
        ArrayList<String> depositArray = new ArrayList<String>(100);
        String[] depositList = config.depositBlacklist().split(",");
        for (String deposit : depositList)
        {
            depositArray.add(deposit.toLowerCase());
        }
        return depositArray;
    }

    public ArrayList<String> getFoodList()
    {
        ArrayList<String> foodArray = new ArrayList<String>(100);
        String[] foodList = config.foodList().split(",");
        for (String food : foodList)
        {
            foodArray.add(food.toLowerCase());
        }
        return foodArray;
    }

    public ArrayList<String> getPotionList()
    {
        ArrayList<String> potionArray = new ArrayList<String>(100);
        String[] potionList = config.potionList().split(",");
        for (String potion : potionList)
        {
            potionArray.add(potion.toLowerCase());
        }
        return potionArray;
    }

    public ArrayList<String> getNpcList()
    {
        ArrayList<String> npcArray = new ArrayList<String>(100);
        String[] npcList = config.npcList().split(",");
        for (String npc : npcList)
        {
            npcArray.add(npc.toLowerCase());
        }
        return npcArray;
    }


    public NPC getInteractingWithNPC()
    {
        NPC npc = null;
        if (client.getLocalPlayer().getInteracting() != null)
        {
            if (client.getLocalPlayer().getInteracting() instanceof NPC)
            {
                npc = (NPC) client.getLocalPlayer().getInteracting();
            }
        }
        return npc;
    }

    public Item getInteractingWithItem()
    {
        Item item = null;
        if (client.getLocalPlayer().getInteracting() != null)
        {
            if (client.getLocalPlayer().getInteracting() instanceof Item)
            {
                item = (Item) client.getLocalPlayer().getInteracting();
            }
        }
        return item;
    }

    public GameObject isInteractingWithGameObject()
    {
        GameObject gameObject = null;
        if (client.getLocalPlayer().getInteracting() != null)
        {
            if (client.getLocalPlayer().getInteracting() instanceof GameObject)
            {
                gameObject = (GameObject) client.getLocalPlayer().getInteracting();
            }
        }
        return gameObject;
    }


    public boolean checkHealth()
    {
        return true;
    }


    public void saveLocation()
    {
        log.info("atv2: INTERACT: saving location");
        savedLocation = client.getLocalPlayer().getWorldLocation();
    }

    public boolean isAtSavedLocation()
    {
        if (savedLocation != null)
        {
            return savedLocation.distanceTo(client.getLocalPlayer().getWorldLocation()) < 5;
        }
        else
        {
            return false;
        }
    }


    @Subscribe
    public void onGameTick(GameTick event)
    {
        if (!currentState.hasEntered())
        {
            currentState.setHasEntered(true);
            switch (currentState) {
                case IDLING:
                    log.info("atv2: IDLING");
                    break;
                case NAVIGATING_TO_ITEM:
                    log.info("atv2: NAVIGATING_TO_ITEM");
                    break;
                case NAVIGATING_TO_NPC:
                    log.info("atv2: NAVIGATING_TO_NPC");
                    break;
                case ATTACKING:
                    log.info("atv2: ATTACKING");
                    // this is monitored on tick to see when the player is no longer attacking
                    break;
                case LOOTING:
                    log.info("atv2: LOOTING");
                    // this is monitored on tick to see when the player is no longer looting
                    break;
                case EATING:
                    log.info("atv2: EATING");
                    break;
                case USING_POTIONS:
                    log.info("atv2: USING_POTIONS");
                    break;
                case BANKING:
                    log.info("atv2: BANKING");
                    break;
                case OPENING_BANK:
                    log.info("atv2: OPENING_BANK");
                    break;
                case NAVIGATING_TO_BANK:
                    log.info("atv2: NAVIGATING_TO_BANK");
                    saveLocation();
                    break;
                case NAVIGATING_BACK_FROM_BANK:
                    log.info("atv2: NAVIGATING_BACK_FROM_BANK");
                    break;
                case BURYING:
                    log.info("atv2: BURYING");
                    break;
                case LOGGED_OUT:
                    break;
                case LOGGING_IN:
                    break;
            }
        }
    }
}