package me.timwastaken.minematics;

import me.timwastaken.minematics.commands.MinematicCommands;
import me.timwastaken.minematics.engine.MinematicEngine;
import me.timwastaken.minematics.models.renderers.BlockDisplayRenderer;
import me.timwastaken.minematics.models.renderers.IMinematicEntityRenderer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minematics extends JavaPlugin {
    private static final IMinematicEntityRenderer renderer = new BlockDisplayRenderer();
    private static int taskID = -1;

    @Override
    public void onEnable() {
        // Plugin startup logic
        MinematicCommands.registerCommands();

        // run minematic engine
        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            MinematicEngine.getInstance().tick();
            MinematicEngine.getInstance().render(renderer);
        }, 0L, 1L);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (renderer instanceof BlockDisplayRenderer blockDisplayRenderer) {
            blockDisplayRenderer.cleanup();
        }
        Bukkit.getServer().getScheduler().cancelTask(taskID);
    }
}
