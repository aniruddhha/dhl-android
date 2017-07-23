package com.melayer.wochat.db.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.FileInputStream;

/**
 * Created by aniruddha on 14/2/17.
 */

public class AppPrefs {

    public static final String FILE_NAME = "woIdPrefs";
    public static final String KEY_WO_ID ="woId";

    public static final String FILE_DASHBOARD = "dashboard";
    public static final String KEY_DASHBOARD_OPEN ="isDashboardOpen";

    public static final String FILE_CHAT = "chat";
    public static final String KEY_CHAT_OPEN ="isChatOpen";

    public static final String FILE_STATUS = "statusFile";
    public static final String KEY_STATUS = "status";

    public static final String TAG = AppPrefs.class.getCanonicalName();

    private final Context context;
    public AppPrefs(Context context){
        this.context = context;
    }
    public void woId(String woId) {
        SharedPreferences prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_WO_ID, woId);
        editor.apply();
    }

    public String woId() {
        SharedPreferences prefs = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return  prefs.getString(KEY_WO_ID,"none");
    }

    public void markDashboardOpen(Boolean isOpen) {
        markOpen(FILE_DASHBOARD, KEY_DASHBOARD_OPEN, isOpen);
    }

    public Boolean isDashBoardOpen() {
        return isOpen(FILE_DASHBOARD, KEY_DASHBOARD_OPEN);
    }

    public void markChatOpen(Boolean isOpen) {
        markOpen(FILE_CHAT, KEY_CHAT_OPEN, isOpen);
    }

    public Boolean isChatOpen() {
        return isOpen(FILE_CHAT, KEY_CHAT_OPEN);
    }

    private void markOpen(String file, String key, Boolean isOpen) {
        Log.i(TAG, "File - "+ file +" Key - "+key + " Is Open - "+isOpen);

        SharedPreferences prefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, isOpen);
        editor.apply();
    }

    private Boolean isOpen(String file, String key) {
        Log.i(TAG, "File - "+ file +" Key - "+key );
        SharedPreferences prefs = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, false);
    }

    public void status(String status) {
        SharedPreferences prefs = context.getSharedPreferences(FILE_STATUS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_STATUS, status);
        editor.apply();
    }

    public String status() {
        SharedPreferences prefs = context.getSharedPreferences(FILE_STATUS, Context.MODE_PRIVATE);
        return prefs.getString(KEY_STATUS, "");
    }
}
