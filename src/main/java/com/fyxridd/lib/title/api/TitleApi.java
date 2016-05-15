package com.fyxridd.lib.title.api;

import com.fyxridd.lib.title.TitlePlugin;
import org.bukkit.entity.Player;

public class TitleApi {
    /**
     * @see #sendTitleAll(String, String, boolean, int)
     */
    public static void sendTitleAll(String title, String subTitle, boolean instant) {
        TitlePlugin.instance.getTitleManager().sendTitleAll(title, subTitle, instant);
    }

    /**
     * 给所有玩家发送标题
     * @param title 标题,可为null
     * @param subTitle 子标题,可为null
     * @param instant 是否立即显示
     * @param time 显示多长时间,单位tick
     */
    public static void sendTitleAll(String title, String subTitle, boolean instant, int time) {
        TitlePlugin.instance.getTitleManager().sendTitleAll(title, subTitle, instant, time);
    }

    /**
     * @see #sendTitle(Player, String, String, boolean, int)
     */
    public static void sendTitle(Player p, String title, String subTitle, boolean instant) {
        TitlePlugin.instance.getTitleManager().sendTitle(p, title, subTitle, instant);
    }

    /**
     * 给玩家发送标题
     * @param p 玩家
     * @param title 标题,可为null
     * @param subTitle 子标题,可为null
     * @param instant 是否立即显示
     * @param time 显示多长时间,单位tick
     */
    public static void sendTitle(Player p, String title, String subTitle, boolean instant, int time) {
        TitlePlugin.instance.getTitleManager().sendTitle(p, title, subTitle, instant, time);
    }
}
