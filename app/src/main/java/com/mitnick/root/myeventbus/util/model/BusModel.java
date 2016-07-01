package com.mitnick.root.myeventbus.util.model;


import android.util.Log;


import com.mitnick.root.myeventbus.util.rxbus.RxBus;
import com.mitnick.root.myeventbus.util.rxbus.RxBusEvent;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by root on 16-5-31.
 * 所有model的父类，即所有的model都必须继承此类并在构造函数中调用subscribe并实现call方法和post方法
 */

public abstract class BusModel implements Observable.OnSubscribe<Object> {

    protected Subscription subscription = null;
    protected String modelName = "";//view通过modelName过滤事件
    protected String event = "";//view通过event判断调用哪个方法
    private RxBusEvent rxBusEvent = null;

    public BusModel(){}

    public BusModel(String modelName){
        this.modelName = modelName;
    }

    public void subscribe(BusModel busModel){
        subscription = Observable.create(busModel)
                .subscribeOn(Schedulers.io())//在io线程上执行耗时操作
                .observeOn(AndroidSchedulers.mainThread())//在UI线程上返回结果
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        Log.e("BusModel","异步任务执行完毕");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("BusModel",e.toString());
                    }

                    @Override
                    public void onNext(Object object) {
                        //post(object);
                        RxBus.getDefault().post(new RxBusEvent(getModelName(),getEvent(),object));
                        unSubscribe();
                    }
                });
    }

    protected void unSubscribe(){
        if(null != subscription){
            subscription.unsubscribe();
        }
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public RxBusEvent getRxBusEvent() {
        return rxBusEvent;
    }

    public void setRxBusEvent(RxBusEvent rxBusEvent) {
        this.rxBusEvent = rxBusEvent;
    }

    @Override
    public void call(Subscriber<? super Object> subscriber){
        subscriber.onNext(doSomethig());
    }

    /**
     * 子类需要实现的耗时操作
     * 该函数内部不能切换线程，否则事件总线会工作不正常
     * 凡是在doSomethig中的代码都运行在线程池中取出的线程，返回结果自动转换为UI线程
     * */
    public abstract Object doSomethig();//返回的Object会转发给View

}
