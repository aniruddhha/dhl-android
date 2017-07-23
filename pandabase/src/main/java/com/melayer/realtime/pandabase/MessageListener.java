package com.melayer.realtime.pandabase;

import mjson.Json;

/**
 * Created by aniruddha on 23/2/17.
 */

public interface MessageListener {
    void onMessage(Json msg);
}
