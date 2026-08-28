package com.firstaidscale.faidhudscale;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.firstaidscale.faidhudscale.client.HudScaleHandler;
import com.firstaidscale.faidhudscale.config.HudScaleConfig;

/**
 * First Aid のボディHUD(体力表示)を config でスケーリングするための
 * 軽量アドオンMOD。
 *
 * First Aid 本体のクラスには一切依存せず、Forge が提供している
 * 公式のGUIオーバーレイAPI (RenderGuiOverlayEvent) だけを使って
 * 対象オーバーレイの描画をキャンセルし、拡縮した状態で描き直す
 * という仕組みになってるよ。だから First Aid のバージョンが
 * 多少変わってもクラス名の不一致でクラッシュすることはないはず。
 */
@Mod(FirstAidHudScale.MOD_ID)
public class FirstAidHudScale {

    public static final String MOD_ID = "faidhudscale";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public FirstAidHudScale() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, HudScaleConfig.SPEC);

        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.register(new HudScaleHandler());
            LOGGER.info("[{}] HUD scale handler registered.", MOD_ID);
        }
    }
}
