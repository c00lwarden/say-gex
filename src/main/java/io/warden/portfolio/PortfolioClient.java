package io.warden.portfolio;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Entry point for a deliberately client-only, non-gameplay-affecting demo mod. */
public final class PortfolioClient implements ClientModInitializer {
    public static final String MOD_ID = "portfolio_client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MOD_ID, "general"));

    private static final KeyMapping TOGGLE_HUD = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                    "key.portfolio_client.toggle_hud",
                    InputConstants.Type.KEYSYM,
                    InputConstants.KEY_F8,
                    CATEGORY));

    @Override
    public void onInitializeClient() {
        DiagnosticHud.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (TOGGLE_HUD.consumeClick()) {
                DiagnosticHud.toggle();
                LOGGER.info("Diagnostics HUD: {}", DiagnosticHud.isVisible() ? "enabled" : "disabled");
            }
            DiagnosticHud.onClientTick();
        });

        LOGGER.info("Portfolio Client loaded. Press F8 to toggle diagnostics.");
    }
}
