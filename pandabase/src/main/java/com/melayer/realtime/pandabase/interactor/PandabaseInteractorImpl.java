package com.melayer.realtime.pandabase.interactor;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.melayer.realtime.pandabase.LocalBinder;
import com.melayer.realtime.pandabase.PandabaseService;
import com.melayer.realtime.pandabase.PandabaseServiceBindListener;

/**
 * Created by aniruddha on 21/2/17.
 */

public class PandabaseInteractorImpl implements PandabaseInteractor {

    public static final String TAG = PandabaseInteractorImpl.class.getCanonicalName();

    private Context context;
    private Intent intentVertxService;
    private PandabaseServiceBindListener bindListener;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceBinder) {
            Log.i(TAG, "Service Connected");
            PandabaseService service = ((LocalBinder<PandabaseService>)serviceBinder).getService();
            if(bindListener != null) bindListener.onBind(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "Service Disconnected");
            if(bindListener != null)
                bindListener.onBind(null);
        }
    };

    public PandabaseInteractorImpl(Context context) {
        this.context = context;
        intentVertxService = new Intent(context, PandabaseService.class);
    }

    @Override
    public void start() {
        context.startService(intentVertxService);
    }

    @Override
    public void stop() {
        context.stopService(intentVertxService);
    }

    @Override
    public void bind(PandabaseServiceBindListener listener) {
        context.bindService(intentVertxService, connection, Context.BIND_AUTO_CREATE);
        this.bindListener = listener;
    }

    @Override
    public void unbind() {
        context.unbindService(connection);
    }

    @Override
    public void bindListener(PandabaseServiceBindListener listener) {
        this.bindListener = listener;
    }
}
