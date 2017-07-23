package com.melayer.realtime.pandabase;

import android.os.Binder;

import java.lang.ref.WeakReference;

/**
 * Created by aniruddha on 15/2/17.
 */

public class LocalBinder<T> extends Binder {

    private final WeakReference<T> service;

    public LocalBinder(T service) {
        this.service = new WeakReference<>(service);
    }

    public T getService(){
        return service.get();
    }
}
