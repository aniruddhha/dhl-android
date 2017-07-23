package com.melayer.realtime.pandabase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.melayer.realtime.pandabase.PandabaseService;
import com.melayer.util.Network;

public class AppReceiver extends BroadcastReceiver {

    private static final String TAG = AppReceiver.class.getCanonicalName();

    public static final String KEY_STARTED_FROM = "startedFrom";

    public static final int FROM_NW_CHANGED = 1;
    public static final int FROM_BOOT_COMPLETED = 2;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, intent.getAction());
        final Intent serviceIntent = new Intent(context, PandabaseService.class);

        if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            serviceIntent.putExtra(KEY_STARTED_FROM, FROM_NW_CHANGED);
        }
        else if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            serviceIntent.putExtra(KEY_STARTED_FROM, FROM_BOOT_COMPLETED);
        }
        if(Network.isConnected(context)) {
            Log.i(TAG, "Network Connected");
            context.startService(serviceIntent);
        }
    }
}
