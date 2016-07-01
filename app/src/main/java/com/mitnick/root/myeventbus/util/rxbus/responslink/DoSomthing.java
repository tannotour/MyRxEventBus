package com.mitnick.root.myeventbus.util.rxbus.responslink;


import android.support.annotation.Nullable;
import android.util.Log;


import com.mitnick.root.myeventbus.util.rxbus.RxBusEvent;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by root on 16-5-17.
 */
public abstract class DoSomthing {

    private String event = null;
    private RxBusEvent rxBusEvent = null;

    public DoSomthing(@Nullable String event){
        this.event = event;
    }

    /**
     * 开始链循环
     * */
    public boolean startDo(RxBusEvent rxBusEvent){
        setRxBusEvent(rxBusEvent);
        if(null == event){
            return dosomthing();
        }else{
            if(filter()){
                //匹配成功
                Log.e("DoSomthing",rxBusEvent.getEvent());
                return dosomthing();
            }
            return false;
        }
    }

    public boolean filter(){
        if(null == event){
            return true;
        }
        return event.equals(rxBusEvent.getEvent());
    }

    public abstract boolean dosomthing();

    public RxBusEvent getRxBusEvent() {
        return rxBusEvent;
    }

    public void setRxBusEvent(RxBusEvent rxBusEvent) {
        this.rxBusEvent = rxBusEvent;
    }

}
