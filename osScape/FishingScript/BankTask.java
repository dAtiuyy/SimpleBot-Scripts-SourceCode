package FishingScript;

import FishingScript.FishingScript;
import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleItem;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

public class BankTask extends Task {
    int id[] = {377,383};
    public BankTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return ctx.inventory.inventoryFull();
    }

    @Override
    public void run() {
        SimpleItem fish = ctx.inventory.populate().filter(id).next();
        SimpleObject bank = ctx.objects.populate().filter(10355).nearest().next();
        if (bank == null) {
            return;
        }
        if (!ctx.bank.bankOpen()) {
            FishingScript.status("Open deposit box");
            bank.menuAction("Deposit");
            ctx.sleepCondition(() -> ctx.bank.bankOpen(), 2500);
        }
        if (ctx.inventory.inventoryFull() && ctx.bank.bankOpen()) {
            FishingScript.status("Banking 1");
            if (fish != null) {
                fish.menuAction("Deposit-All");
            }
            ctx.sleepCondition(() -> !ctx.inventory.inventoryFull(), 2500);
        }
        if (ctx.bank.bankOpen() && !ctx.inventory.inventoryFull()) {
            FishingScript.status("Closing Bank");
            ctx.bank.closeBank();
            ctx.sleepCondition(() -> !ctx.bank.bankOpen(), 2500);
        }
    }

    @Override
    public String status() {
        return "";
    }
}