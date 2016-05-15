package com.fyxridd.lib.title.config;

import com.fyxridd.lib.config.api.basic.Path;
import com.fyxridd.lib.config.api.limit.Min;

public class TitleConfig {
    @Path("time")
    @Min(0)
    private int time;

    @Path("interval")
    @Min(0)
    private int interval;

    @Path("fadeIn")
    @Min(0)
    private int fadeIn;

    @Path("fadeOut")
    @Min(0)
    private int fadeOut;

    public int getTime() {
        return time;
    }

    public int getInterval() {
        return interval;
    }

    public int getFadeIn() {
        return fadeIn;
    }

    public int getFadeOut() {
        return fadeOut;
    }
}
