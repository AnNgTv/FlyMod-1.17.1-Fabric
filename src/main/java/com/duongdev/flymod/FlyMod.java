package com.duongdev.flymod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class FlyMod implements ModInitializer {
    private static KeyBinding flyKey;
    private boolean isFlyActive = false;

    @Override
    public void onInitialize() {
        // 1. Đăng ký phím tắt (Mặc định là phím R)
        flyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.flymod.toggle", 
                InputUtil.Type.KEYSYM, 
                GLFW.GLFW_KEY_R, 
                "category.flymod"
        ));

        // 2. Lắng nghe sự kiện mỗi Tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (flyKey.wasPressed()) {
                isFlyActive = !isFlyActive;
                if (client.player != null) {
                    String status = isFlyActive ? "§aON" : "§cOFF";
                    client.player.sendMessage(new LiteralText("Flight: " + status), true);
                }
            }

            if (client.player != null) {
                // Ép buộc khả năng bay dựa trên trạng thái biến isFlyActive
                client.player.getAbilities().allowFlying = isFlyActive;
                
                // Nếu tắt mod khi đang bay, người chơi sẽ rơi xuống
                if (!isFlyActive) {
                    client.player.getAbilities().flying = false;
                }
                
                client.player.sendAbilitiesUpdate();
            }
        });
    }
}
