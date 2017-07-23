package com.melayer.dhlbot.io;

import com.melayer.realtime.pandabase.MessageListener;
import com.melayer.realtime.pandabase.PandabaseService;

import mjson.Json;

/**
 * Created by aniruddha on 22/7/17.
 */

public interface Io {

    void request(Json msg);

    void response(MessageListener handler);

    void service(PandabaseService service);
}
