package me.timwastaken.minematics;

import me.timwastaken.minematics.commands.MinematicCommands;
import me.timwastaken.minematics.common.exceptions.CannotReachException;
import me.timwastaken.minematics.engine.MinematicEngine;
import me.timwastaken.minematics.events.ItemActionRegistry;
import me.timwastaken.minematics.events.PlayerItemEvents;
import me.timwastaken.minematics.models.MinematicFabrikChain;
import me.timwastaken.minematics.models.MinematicFactory;
import me.timwastaken.minematics.models.renderers.BlockDisplayRenderer;
import me.timwastaken.minematics.models.renderers.IMinematicEntityRenderer;
import me.timwastaken.minematics.models.template.MinematicEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public final class Minematics extends JavaPlugin {
    private static final IMinematicEntityRenderer renderer = new BlockDisplayRenderer();
    private static int taskID = -1;

    @Override
    public void onEnable() {
        // Plugin startup logic
        MinematicCommands.registerCommands();

        ItemActionRegistry.insertAction(Material.ARROW, "FABRIK", (player) -> {
            MinematicEngine.getInstance().runAction((entity) -> {
                if (entity instanceof MinematicFabrikChain chain) {
                    try {
                        chain.reachTo(player.getEyeLocation());
                        player.playSound(chain.getBase().getPosition(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, 0.5f);
                    } catch (CannotReachException e) {
                        player.playSound(chain.getBase().getPosition(), Sound.BLOCK_AZALEA_LEAVES_BREAK, 1, 1);
                    }
                }
            });
        });
        ItemActionRegistry.insertAction(Material.BLAZE_ROD, "Straighten", (player) -> {
            MinematicEngine.getInstance().runAction((entity) -> {
                if (entity instanceof MinematicFabrikChain chain) {
                    player.playSound(chain.getBase().getPosition(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
                    chain.straightenTowards(player.getEyeLocation());
                }
            });
        });

        // run minematic engine
        taskID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            MinematicEngine.getInstance().tick();
            MinematicEngine.getInstance().render(renderer);
        }, 0L, 1L);

        Bukkit.getPluginManager().registerEvents(new PlayerItemEvents(), this);
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
