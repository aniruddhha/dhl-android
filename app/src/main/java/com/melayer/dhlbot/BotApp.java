package com.melayer.dhlbot;

import android.app.Application;
import android.util.Log;

import com.melayer.dhlbot.io.BotIo;
import com.melayer.dhlbot.io.Io;
import com.melayer.realtime.pandabase.MessageListener;
import com.melayer.realtime.pandabase.PandabaseService;
import com.melayer.realtime.pandabase.interactor.PandabaseInteractor;
import com.melayer.realtime.pandabase.interactor.PandabaseInteractorImpl;
import com.melayer.util.App;
import com.melayer.wochat.db.prefs.AppPrefs;

/**
 * Created by aniruddha on 22/7/17.
 */

public class BotApp extends Application {

    private AppPrefs appPrefs;

    private PandabaseInteractor pandabaseInteractor;
    private Io io;
    private MessageListener msgListener;

    @Override
    public void onCreate() {
        super.onCreate();

        pandabaseInteractor = new PandabaseInteractorImpl(this);

        io = new BotIo();

        if (!App.isServiceRunning(this, PandabaseService.class)) {
            Log.i("@melayer", "Staring service");
            pandabaseInteractor.start();
        }
        Log.i("@melayer", "Already service running");

        pandabaseInteractor.bind(this::onBind);
    }

    private void onBind(PandabaseService pandabaseService) {
        io.service(pandabaseService);
        pandabaseService.connectionListener(isConnected -> io.response(msg -> {
            msgListener.onMessage(msg);
        }));
    }

    public Io io() {
        return io;
    }

    public PandabaseInteractor pandabaseInteractor() {
        return pandabaseInteractor;
    }

    public void messageListener(MessageListener msg) {
        this.msgListener = msg;
    }
}
