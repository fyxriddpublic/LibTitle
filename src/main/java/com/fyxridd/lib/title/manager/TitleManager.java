package com.fyxridd.lib.title.manager;

import com.comphenix.packetwrapper.WrapperPlayServerTitle;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.fyxridd.lib.config.api.ConfigApi;
import com.fyxridd.lib.config.manager.ConfigManager;
import com.fyxridd.lib.title.TitlePlugin;
import com.fyxridd.lib.title.config.TitleConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;

import java.util.*;

public class TitleManager {
    //标题信息
    private class Info {
        private int time;
        //不为null可为""
        private String title;
        //不为null可为""
        private String subTitle;

        public Info(int time, String title, String subTitle) {
            this.time = time;
            this.title = title;
            this.subTitle = subTitle;
        }
    }

    private TitleConfig config;

    private Map<Player, List<Info>> infos = new HashMap<>();
    //玩家 当前正在显示信息,需要等待的tick
    private Map<Player, Integer> waits = new HashMap<>();

    public TitleManager() {
        //添加配置监听
        ConfigApi.addListener(TitlePlugin.instance.pn, TitleConfig.class, new ConfigManager.Setter<TitleConfig>() {
            @Override
            public void set(TitleConfig value) {
                config = value;
            }
        });
        //注册事件
        {
            //玩家退出
            Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, TitlePlugin.instance, EventPriority.LOW, new EventExecutor() {
                @Override
                public void execute(Listener listener, Event e) throws EventException {
                    PlayerQuitEvent event = (PlayerQuitEvent) e;
                    infos.remove(event.getPlayer());
                    waits.remove(event.getPlayer());
                }
            }, TitlePlugin.instance);
        }
        //计时器: 检测下一个
        Bukkit.getScheduler().scheduleSyncRepeatingTask(TitlePlugin.instance, new Runnable() {
            @Override
            public void run() {
                //检测过期
                Iterator<Map.Entry<Player, Integer>> it = waits.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Player, Integer> entry = it.next();
                    if (entry.getValue() <= 1) it.remove();
                    else entry.setValue(entry.getValue()-1);
                }
                //检测下一个
                for (Player p:Bukkit.getOnlinePlayers()) {
                    if (!waits.containsKey(p)) {
                        List<Info> list = init(p);
                        if (!list.isEmpty()) {
                            Info info = list.remove(0);
                            show(p, info.time, info.title, info.subTitle);
                        }
                    }
                }
            }
        }, 1, 1);
    }

    /**
     * @see com.fyxridd.lib.title.api.TitleApi#sendTitleAll(String, String, boolean)
     */
    public void sendTitleAll(String title, String subTitle, boolean instant) {
        int time = ((title == null?0:title.length())+(subTitle == null?0:subTitle.length()))*config.getTime();
        sendTitleAll(title, subTitle, instant, time);
    }

    /**
     * @see com.fyxridd.lib.title.api.TitleApi#sendTitleAll(String, String, boolean, int)
     */
    public void sendTitleAll(String title, String subTitle, boolean instant, int time) {
        for (Player p:Bukkit.getOnlinePlayers()) sendTitle(p, title, subTitle, instant, time);
    }

    /**
     * @see com.fyxridd.lib.title.api.TitleApi#sendTitle(Player, String, String, boolean)
     */
    public void sendTitle(Player p, String title, String subTitle, boolean instant) {
        int time = ((title == null?0:title.length())+(subTitle == null?0:subTitle.length()))*config.getTime();
        sendTitle(p, title, subTitle, instant, time);
    }

    /**
     * @see com.fyxridd.lib.title.api.TitleApi#sendTitle(Player, String, String, boolean, int)
     */
    public void sendTitle(Player p, String title, String subTitle, boolean instant, int time) {
        if (instant) show(p, time, title, subTitle);
        else init(p).add(new Info(time, title == null ? "" : title, subTitle == null ? "" : subTitle));
    }

    /**
     * 强制玩家立即显示信息
     */
    private void show(Player p, int time, String title, String subTitle) {
        if (title == null) title = "";
        if (subTitle == null) subTitle = "";

        waits.put(p, time+config.getInterval()+config.getFadeIn()+config.getFadeOut());

        WrapperPlayServerTitle packet;

        //清除
        packet = new WrapperPlayServerTitle();
        packet.setAction(EnumWrappers.TitleAction.CLEAR);
        packet.sendPacket(p);

        //时间
        packet = new WrapperPlayServerTitle();
        packet.setAction(EnumWrappers.TitleAction.TIMES);
        packet.setFadeIn(config.getFadeIn());
        packet.setStay(time);
        packet.setFadeOut(config.getFadeOut());
        packet.sendPacket(p);

        //标题
        packet = new WrapperPlayServerTitle();
        packet.setAction(EnumWrappers.TitleAction.TITLE);
        packet.setTitle(WrappedChatComponent.fromText(title));
        packet.sendPacket(p);

        //副标题
        packet = new WrapperPlayServerTitle();
        packet.setAction(EnumWrappers.TitleAction.SUBTITLE);
        packet.setTitle(WrappedChatComponent.fromText(subTitle));
        packet.sendPacket(p);
    }

    private List<Info> init(Player p) {
        List<Info> list = infos.get(p);
        if (list == null) {
            list = new ArrayList<>();
            infos.put(p, list);
        }
        return list;
    }
}
