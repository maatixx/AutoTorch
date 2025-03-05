package fr.matix.autotorch;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(autoTorch.MODID)
public class autoTorch {

    public static final String MODID = "autotorch";
    private static final Logger LOGGER = LogUtils.getLogger();

    public autoTorch() {
        MinecraftForge.EVENT_BUS.register(this);
        LOGGER.info("Mod Loaded");
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.side.isClient()) {
            Player player = Minecraft.getInstance().player;
            Level world = Minecraft.getInstance().level;

            if (player != null && world != null) {
                boolean hasTorch = player.getInventory().contains(new ItemStack(Items.TORCH));
                BlockPos playerPos = player.blockPosition();
                int brightness = world.getMaxLocalRawBrightness(playerPos);

                if (hasTorch && brightness < 4) {
                    world.setBlock(playerPos, Blocks.TORCH.defaultBlockState(), 3);
                    // Remove one torch from the player's inventory
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (stack.getItem() == Items.TORCH) {
                            stack.shrink(1);
                            if (stack.isEmpty()) {
                                player.getInventory().setItem(i, ItemStack.EMPTY);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
}