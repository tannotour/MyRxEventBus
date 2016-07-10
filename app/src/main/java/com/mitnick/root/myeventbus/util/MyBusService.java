package com.mitnick.root.myeventbus.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.mitnick.root.myeventbus.R;
import com.mitnick.root.myeventbus.util.net.NetAPIFactory;
import com.mitnick.root.myeventbus.util.rxbus.RxBus;

import rx.Subscription;


/**
 * Created by root on 16-5-30.
 */

public class MyBusService extends Service {

    private Subscription rxSubscription = null;//订阅RxBus事件

    @Override
    public void onCreate() {
        super.onCreate();
        rxSubscription = RxBus.subscription(
                this,
                new String[]{"MyBusService"}//监听的TAG
        );
        NetAPIFactory.netAPIInit(this.getString(R.string.base_server_ip), null);//初始化网络请求
//        RxBus.getDefault().post(new RxBusEvent("MyBusService","login",null));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if(rxSubscription != null && !rxSubscription.isUnsubscribed()){
            rxSubscription.unsubscribe();
        }
        super.onDestroy();
    }
}
