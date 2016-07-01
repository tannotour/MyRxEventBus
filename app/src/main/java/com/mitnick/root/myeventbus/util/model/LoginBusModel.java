package com.mitnick.root.myeventbus.util.model;

import android.util.Log;

/**
 * Created by root on 16-6-23.
 */

public class LoginBusModel extends BusModel {

    public LoginBusModel(){
//        subscribe(this);
//        setModelName("com.mitnick.root.app.carpool.model.LoginBusModel");
//        Log.i("LoginBusModel","初始化成功");
    }

    @Override
    public Object doSomethig() {
        Log.e("LoginBusModel","初始化成功");
        setEvent("loginOk");
        return null;
    }
}
