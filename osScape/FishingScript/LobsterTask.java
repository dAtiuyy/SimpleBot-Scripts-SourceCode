package FishingScript;

import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleNpc;
import simple.robot.api.ClientContext;

public class LobsterTask extends Task {
    public LobsterTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return !ctx.inventory.inventoryFull() && ctx.skills.realLevel(SimpleSkills.Skills.FISHING) <= 76 && ctx.inventory.populate().filter(301).next() != null;
        }

    @Override
    public void run() {
        SimpleNpc spot = ctx.npcs.populate().filter(1519).nextNearest();
        if (ctx.players.getLocal().getAnimation() == -1) {
            if (spot != null) {
                FishingScript.status("Lobster");
                spot.menuAction("Cage");
                ctx.sleep(2500);
            }
        } else {
            ctx.sleepCondition(() -> ctx.players.getLocal().getAnimation() == -1, 2500);
        }
    }

    @Override
    public String status() {
        return "";
    }
}
