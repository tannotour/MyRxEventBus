package com.mitnick.root.myeventbus;

import android.app.Application;
import android.content.Intent;

import com.mitnick.root.myeventbus.util.MyBusService;

/**
 * Created by root on 16-6-23.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyBusService.class));//启动总线server
    }
}
