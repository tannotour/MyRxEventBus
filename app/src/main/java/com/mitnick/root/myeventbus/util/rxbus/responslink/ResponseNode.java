package com.mitnick.root.myeventbus.util.rxbus.responslink;



import com.mitnick.root.myeventbus.util.rxbus.RxBusEvent;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 16-5-17.
 */
public class ResponseNode implements Handler {

    private DoSomthing doSomthing;
    private Handler handler;

    public ResponseNode(DoSomthing doSomthing){
        this.doSomthing = doSomthing;
    }

    public Handler getHandler() {
        return handler;
    }

    public ResponseNode setHandler(ResponseNode handler) {
        this.handler = handler;
        return handler;
    }

    @Override
    public void operator(final RxBusEvent rxBusEvent) {
        //本节点操作
        if(!doSomthing.startDo(rxBusEvent)){
            //本节点无法处理事件，交给下一个节点处理
            if(getHandler() != null){
                getHandler().operator(rxBusEvent);
            }
        }
    }
}
