package com.melayer.realtime.pandabase.interactor;


import com.melayer.realtime.pandabase.PandabaseServiceBindListener;

/**
 * Created by aniruddha on 21/2/17.
 */

public interface PandabaseInteractor {

    void start();

    void stop();

    void bind(PandabaseServiceBindListener listener);

    void unbind();

    @Deprecated
    void bindListener(PandabaseServiceBindListener listener);
}
