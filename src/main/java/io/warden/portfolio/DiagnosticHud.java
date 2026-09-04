package io.warden.portfolio;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.DeltaTracker;
import net.minecraft.resources.Identifier;

/**
 * A low-cost local diagnostic overlay. It reads only this computer's Minecraft
 * process and never contacts a server or changes gameplay.
 */
public final class DiagnosticHud {
    private static boolean visible = true;
    private static long startedAt = Util.getMillis();
    private static long ticks;
    private static long lastSampleAt;
    private static int sampledFps;
    private static long usedMemoryMiB;
    private static long maxMemoryMiB;

    private DiagnosticHud() { }

    public static void register() {
        HudElementRegistry.attachElementBefore(
                VanillaHudElements.CHAT,
                Identifier.fromNamespaceAndPath(PortfolioClient.MOD_ID, "diagnostics"),
                DiagnosticHud::render);
    }

    public static void onClientTick() {
        ticks++;
        long now = Util.getMillis();

        // Sampling once per second avoids turning a diagnostics overlay into a cost itself.
        if (now - lastSampleAt < 1_000L) return;
        lastSampleAt = now;

        Runtime runtime = Runtime.getRuntime();
        usedMemoryMiB = (runtime.totalMemory() - runtime.freeMemory()) / 1_048_576L;
        maxMemoryMiB = runtime.maxMemory() / 1_048_576L;
        sampledFps = Minecraft.getInstance().getFps();
    }

    private static void render(GuiGraphicsExtractor graphics, DeltaTracker ignoredDelta) {
        if (!visible) return;

        Minecraft client = Minecraft.getInstance();
        long elapsedSeconds = (Util.getMillis() - startedAt) / 1_000L;
        String uptime = String.format("%02d:%02d", elapsedSeconds / 60L, elapsedSeconds % 60L);

        int x = 8;
        int y = 8;
        int line = client.font.lineHeight + 2;
        int color = 0xFFE8F5E9;

        graphics.fill(x - 4, y - 4, x + 188, y + line * 5 + 4, 0xAA101418);
        graphics.text(client.font, "Portfolio Client — local diagnostics", x, y, color, true);
        graphics.text(client.font, "FPS: " + sampledFps, x, y + line, color, true);
        graphics.text(client.font, "Memory: " + usedMemoryMiB + " / " + maxMemoryMiB + " MiB", x, y + line * 2, color, true);
        graphics.text(client.font, "Client uptime: " + uptime, x, y + line * 3, color, true);
        graphics.text(client.font, "Ticks observed: " + ticks + "  |  F8 hides HUD", x, y + line * 4, color, true);
    }

    public static void toggle() {
        visible = !visible;
    }

    public static boolean isVisible() {
        return visible;
    }
}
