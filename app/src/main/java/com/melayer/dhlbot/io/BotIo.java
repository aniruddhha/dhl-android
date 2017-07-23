package com.melayer.dhlbot.io;

import com.melayer.realtime.pandabase.MessageListener;
import com.melayer.realtime.pandabase.PandabaseService;

import mjson.Json;

/**
 * Created by aniruddha on 22/7/17.
 */

public class BotIo implements Io {

    private PandabaseService service;

    @Override
    public void request(Json msg) {
        service.send("bot.DHL123", msg);
    }

    @Override
    public void response(MessageListener handler) {
        service.consumer("panda.gaga123", handler);
    }

    @Override
    public void service(PandabaseService service) {
        this.service = service;
    }
}
