package me.timwastaken.minematics.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.*;
import me.timwastaken.minematics.engine.MinematicEngine;
import me.timwastaken.minematics.models.MinematicChain;
import me.timwastaken.minematics.models.MinematicFactory;
import me.timwastaken.minematics.models.template.MinematicEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class MinematicCommands {
    public static void registerCommands() {
        // Register commands here
        new CommandAPICommand("chain")
                .withArguments(
                        new LocationArgument("baseBlock", LocationType.BLOCK_POSITION),
                        new ItemStackArgument("itemMaterial"),
                        new IntegerArgument("numSegments", 1),
                        new FloatArgument("chainLength", 0, 100)
                )
                .withOptionalArguments(
                        new FloatArgument("baseWidth",0.1f, 1),
                        new FloatArgument("topWidth", 0.1f, 1)
                )
                .withPermission(CommandPermission.OP)
                .executes((sender, args) -> {
                    ItemStack itemMaterial = (ItemStack) args.get("itemMaterial");
                    Location baseBlock = (Location) args.get("baseBlock");
                    int numSegments = (int) args.get("numSegments");
                    float chainLength = (float) args.get("chainLength");
                    float baseWidth = (float) args.getOptional("baseWidth").orElse(0.5f);
                    float topWidth = (float) args.getOptional("topWidth").orElse(0.2f);

                    float segmentLength = chainLength / numSegments;

                    MinematicEntity chain = new MinematicChain(
                            itemMaterial.getType(),
                            baseBlock,
                            segmentLength,
                            numSegments,
                            baseWidth,
                            topWidth
                    );

                    MinematicEngine.getInstance().trackEntity(chain);
                })
                .register();

        new CommandAPICommand("bb")
                .withArguments(
                        new ItemStackArgument("material"),
                        new LocationArgument("position")
                )
                .withPermission(CommandPermission.OP)
                .executesPlayer((sender, args) -> {
                    ItemStack materialStack = (ItemStack) args.get("material");
                    Location position = (Location) args.get("position");
                    Material material = materialStack.getType();

                    MinematicEntity blockEntity = MinematicFactory.createMinematicBlockEntity(
                            material,
                            position,
                            new Vector(1, 1, 1),
                            new Vector(0, 0, 0),
                            new Vector(1, 0, 0)
                    );
                    MinematicEngine.getInstance().trackEntity(blockEntity);
                })
                .register();
    }
}
