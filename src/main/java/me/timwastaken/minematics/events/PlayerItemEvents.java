package me.timwastaken.minematics.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

public class PlayerItemEvents implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // only handle right clicks
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        // only handle if player did not interact
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                !(!event.getClickedBlock().getType().isInteractable() || event.getPlayer().isSneaking())) return;

        Consumer<Player> action = ItemActionRegistry.getActionFor(event.getItem());
        if (action == null) return;
        action.accept(event.getPlayer());
    }
}
