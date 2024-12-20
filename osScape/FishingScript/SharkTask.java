package FishingScript;

import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

public class SharkTask extends Task {
    public SharkTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return !ctx.inventory.inventoryFull() && ctx.skills.realLevel(SimpleSkills.Skills.FISHING) >= 76;
        //return !ctx.inventory.inventoryFull() && ctx.skills.realLevel(SimpleSkills.Skills.FISHING) >= 76 && ctx.skills.realLevel(SimpleSkills.Skills.FISHING) <= 81;
    }

    @Override
    public void run() {
        SimpleNpc spot = ctx.npcs.populate().filter(1520).nextNearest();
        if (ctx.players.getLocal().getAnimation() == -1) {
            if (spot != null) {
                FishingScript.status("Fishing");
                spot.menuAction("Harpoon");
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
