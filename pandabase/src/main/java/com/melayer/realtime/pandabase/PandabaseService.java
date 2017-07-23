package com.melayer.realtime.pandabase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.melayer.realtime.pandabase.receiver.AppReceiver;
import com.melayer.vertx.client.EventBus;
import com.melayer.vertx.client.handlers.ConnectHandler;
import com.melayer.vertx.client.handlers.Handler;

import mjson.Json;

public final class PandabaseService extends Service {

    private static String TAG = PandabaseService.class.getCanonicalName();
    private final EventBus eventBus = new EventBus();
    private ConnectionListener connectionListener;
    private NetworkListener networkListener;

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder<>(this);
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {

        Log.i(TAG, "onStartCommand");
        ifNetworkChanged(intent);
        connect();

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        if (eventBus.isConnected()) {
            eventBus.close();
            Log.i(TAG, "Vertx Connection Closed");
        }

        super.onDestroy();
    }

    public void connectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public void networkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    private void ifNetworkChanged(Intent intent) {
        if (intent != null)
            if (intent.getIntExtra(AppReceiver.KEY_STARTED_FROM, -1) == AppReceiver.FROM_NW_CHANGED)
                if (networkListener != null) networkListener.onNetwork(true);
    }

    private void connect() {
        new Thread(this::runService).start();
    }

    private void runService() {
        Looper.prepare();
        if (!eventBus.isConnected()) {
            eventBus.connect("ec2-52-66-112-58.ap-south-1.compute.amazonaws.com", 8090, this::onPanadabaseConnected);
        } else Log.i(TAG, "Already Connected");
        Looper.loop();
    }

    private void onPanadabaseConnected(boolean isConnected) {
        Log.i(TAG, "Connection status " + isConnected);
        if (isConnected) {
            Log.i(TAG, "Connected");
            if (connectionListener != null) {
                connectionListener.onConnection(true);
            }
        }
    }

    public void consumer(String address, final MessageListener handler) {
        if (isConnected())
            eventBus.registerHandler(address, s -> {
                Log.i("@codekul", "Value is " + s);
                handler.onMessage(Json.read(s).at("body"));
            });
    }

    public void send(String address, Json msg) {
        if (isConnected())
            eventBus.send(address, "" + msg);
    }

    public void publish(String address, Json msg) {
        if (isConnected())
            eventBus.publish(address, "" + msg);
    }

    public void unregister(String address) {
        if (isConnected()) {
            eventBus.unregisterHandler(address);
        }
    }

    public boolean isConnected() {
        return eventBus.isConnected();
    }
}
