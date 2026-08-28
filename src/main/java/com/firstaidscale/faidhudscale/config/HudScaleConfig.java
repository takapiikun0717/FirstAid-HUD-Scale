package com.firstaidscale.faidhudscale.config;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * config/faidhudscale-client.toml に保存されるクライアント専用コンフィグ。
 * 頻繁に変更するものじゃないから、ゲーム内オプションじゃなくて
 * config ファイルで管理するようにしてあるよ。
 */
public class HudScaleConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.DoubleValue SCALE;
    public static final ForgeConfigSpec.ConfigValue<String> OVERLAY_ID;
    public static final ForgeConfigSpec.DoubleValue ANCHOR_X;
    public static final ForgeConfigSpec.DoubleValue ANCHOR_Y;
    public static final ForgeConfigSpec.BooleanValue LOG_OVERLAY_IDS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("hud_scale");

        ENABLED = builder
                .comment(
                        " First Aid のボディHUD(体力表示)の拡大縮小機能を有効にするかどうか。"
                )
                .define("enabled", true);

        SCALE = builder
                .comment(
                        " HUDの拡大率だよ。",
                        " 1.0 = 等倍(通常サイズ)",
                        " 2.0 = 2倍の大きさ",
                        " 0.5 = 半分の大きさ",
                        " 範囲: 0.25 ~ 4.0"
                )
                .defineInRange("scale", 1.0, 0.25D, 4.0D);

        OVERLAY_ID = builder
                .comment(
                        " 拡大したいHUDオーバーレイのID(namespace:path の形式)。",
                        " First Aid が登録しているオーバーレイのIDを指定してね。",
                        " デフォルト値の 'firstaid:player_health' は予想値だから、",
                        " もしうまくサイズが変わらなかったら logOverlayIds を true にして",
                        " 一度ゲームを起動してみて。latest.log に検出した全オーバーレイの",
                        " IDが出力されるから、そこから 'firstaid' から始まるIDを探して",
                        " ここに設定し直してね。"
                )
                .define("overlayId", "firstaid:hud");

        ANCHOR_X = builder
                .comment(
                        " 拡大の基準点(横方向)。0.0 = 画面の左端、1.0 = 画面の右端。",
                        " First Aid のHUD位置を画面左側(デフォルト)にしてるなら 0.0 のままでOK。",
                        " 右側に配置してるなら 1.0 に、中央寄りなら 0.5 とかに調整してね。"
                )
                .defineInRange("anchorX", 0.0D, 0.0D, 1.0D);

        ANCHOR_Y = builder
                .comment(
                        " 拡大の基準点(縦方向)。0.0 = 画面の上端、1.0 = 画面の下端。"
                )
                .defineInRange("anchorY", 0.0D, 0.0D, 1.0D);

        LOG_OVERLAY_IDS = builder
                .comment(
                        " true にすると、ゲーム起動中に検出した全オーバーレイのIDを",
                        " 一度だけ latest.log に出力するよ。overlayId の値を調べるための",
                        " デバッグ機能だから、正しいIDが分かったら false に戻してOK。"
                )
                .define("logOverlayIds", false);

        builder.pop();

        SPEC = builder.build();
    }

    private HudScaleConfig() {
    }
}
