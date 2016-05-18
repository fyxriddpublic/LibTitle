package com.fyxridd.lib.title;

import com.fyxridd.lib.core.api.config.ConfigApi;
import com.fyxridd.lib.core.api.plugin.SimplePlugin;
import com.fyxridd.lib.title.config.TitleConfig;
import com.fyxridd.lib.title.manager.TitleManager;

public class TitlePlugin extends SimplePlugin{
    public static TitlePlugin instance;

    private TitleManager titleManager;

    @Override
    public void onEnable() {
        instance = this;

        //注册配置
        ConfigApi.register(pn, TitleConfig.class);

        titleManager = new TitleManager();

        super.onEnable();
    }

    public TitleManager getTitleManager() {
        return titleManager;
    }
}