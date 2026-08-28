package com.firstaidscale.faidhudscale.client;

import com.firstaidscale.faidhudscale.FirstAidHudScale;
import com.firstaidscale.faidhudscale.config.HudScaleConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * Forge が公式に提供している RenderGuiOverlayEvent だけを使ってHUDを拡縮するクラス。
 *
 * First Aid 内部のクラスには一切触れず、対象オーバーレイのIDが一致した時だけ
 * ・Pre イベントをキャンセルして通常描画を止める
 * ・スケール済みの PoseStack で自前に overlay.render(...) を呼び直す
 * という手順で拡大縮小を実現しているよ。
 */
public class HudScaleHandler {

    private final Set<String> loggedOverlayIds = new HashSet<>();

    @SubscribeEvent
    public void onPreRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        NamedGuiOverlay namedOverlay = event.getOverlay();
        String id = namedOverlay.id().toString();

        // デバッグ用: 起動中に見つけた全オーバーレイIDを一度だけログに出す
        if (HudScaleConfig.LOG_OVERLAY_IDS.get() && loggedOverlayIds.add(id)) {
            FirstAidHudScale.LOGGER.info("[faidhudscale] detected overlay id -> {}", id);
        }

        if (!HudScaleConfig.ENABLED.get()) {
            return;
        }

        if (!id.equals(HudScaleConfig.OVERLAY_ID.get())) {
            return;
        }

        double scale = HudScaleConfig.SCALE.get();
        if (scale == 1.0D) {
            // 等倍なら何もしなくてOK(通常描画に任せる)
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.gui instanceof ForgeGui forgeGui)) {
            return;
        }

        GuiGraphics guiGraphics = event.getGuiGraphics();
        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();

        float anchorX = (float) (HudScaleConfig.ANCHOR_X.get() * screenWidth);
        float anchorY = (float) (HudScaleConfig.ANCHOR_Y.get() * screenHeight);

        // 通常の描画をキャンセルして、自前で拡縮した状態で描き直す
        event.setCanceled(true);

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(anchorX, anchorY, 0);
        poseStack.scale((float) scale, (float) scale, 1.0f);
        poseStack.translate(-anchorX, -anchorY, 0);

        namedOverlay.overlay().render(forgeGui, guiGraphics, event.getPartialTick(), screenWidth, screenHeight);

        poseStack.popPose();
    }
}
