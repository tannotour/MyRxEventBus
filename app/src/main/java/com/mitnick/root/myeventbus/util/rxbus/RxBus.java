package com.mitnick.root.myeventbus.util.rxbus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.mitnick.root.myeventbus.EventMap;
import com.mitnick.root.myeventbus.util.rxbus.responslink.HandlerFactory;
import com.mitnick.root.myeventbus.util.rxbus.responslink.ResponseNode;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by root on 16-4-12.
 * 事件体只会被发射到相对应的主题的订阅者
 */
public class RxBus {
    private static volatile RxBus defaultInstance;
    // 主题
    private final Subject bus;
    public RxBus() {
        bus = new SerializedSubject<>(PublishSubject.create());
    }
    // 单例RxBus
    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 事件总线发射一个事件
     * @Param rxBusEvent : 事件体
     * */
    public void post (RxBusEvent rxBusEvent) {
        bus.onNext(rxBusEvent);
    }

    /**
     * 订阅事件总线的某一个主题
     * @Param tag : 订阅的主题，事件总线只会对感兴趣的主题发射事件
     * @Param eventType : 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     * */
    public <T> Observable<T> toObserverable (Class<T> eventType) {
        return bus.ofType(eventType);
//        这里感谢小鄧子的提醒: ofType = filter + cast
//        return bus.filter(new Func1<Object, Boolean>() {
//            @Override
//            public Boolean call(Object o) {
//                return eventType.isInstance(o);
//            }
//        }) .cast(eventType);
    }

    public static Subscription subscription(@Nullable final String[] tags, final ResponseNode responseNode){
        return getDefault().toObserverable(RxBusEvent.class)
                .filter(new Func1<RxBusEvent, Boolean>() {
                    @Override
                    public Boolean call(RxBusEvent rxBusEvent) {
                        if(null == tags){
                            //没有过滤条件
                            return true;
                        }
                        for(String tag : tags){
                            if(rxBusEvent.getTag().equals(tag)){
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .subscribe(new Action1<RxBusEvent>() {
                               @Override
                               public void call(final RxBusEvent rxBusEvent) {
                                   responseNode.operator(rxBusEvent);//开启职责链处理事件
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                                Log.e("事件总线",throwable.toString());
                            }
                        });
    }

    public static Subscription subscription(@NonNull Context context, @Nullable final String[] tags){
        final ResponseNode responseNode = HandlerFactory.getHandlerAutoDo(EventMap.getServiceMap(context));
        return getDefault().toObserverable(RxBusEvent.class)
                .filter(new Func1<RxBusEvent, Boolean>() {
                    @Override
                    public Boolean call(RxBusEvent rxBusEvent) {
                        if(null == tags){
                            //没有过滤条件
                            return true;
                        }
                        for(String tag : tags){
                            if(rxBusEvent.getTag().equals(tag)){
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .subscribe(new Action1<RxBusEvent>() {
                               @Override
                               public void call(final RxBusEvent rxBusEvent) {
                                   responseNode.operator(rxBusEvent);//开启职责链处理事件
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                                Log.e("事件总线",throwable.toString());
                            }
                        });
    }

    /**
     * 只给activity用
     * */
    public static Subscription viewSubscription(@NonNull Context context,
                                            @Nullable final String[] events,
                                            @Nullable final Map<String,String> reflact){
        final ResponseNode responseNode = HandlerFactory.getHandler(context,reflact);
        final Map<String, String> modelMap = EventMap.getServiceMap(context);
        return getDefault().toObserverable(RxBusEvent.class)
                .filter(new Func1<RxBusEvent, Boolean>() {
                    @Override
                    public Boolean call(RxBusEvent rxBusEvent) {
                        if(null == events){
                            //没有过滤条件
                            return true;
                        }
                        for(String event : events){
                            if(modelMap.containsKey(event)){
                                if(rxBusEvent.getTag().equals(modelMap.get(event))){
                                    return true;//找到了event对应的model名字
                                }
                            }
                        }
                        return false;
                    }
                })
                .subscribe(new Action1<RxBusEvent>() {
                               @Override
                               public void call(final RxBusEvent rxBusEvent) {
                                   responseNode.operator(rxBusEvent);//开启职责链处理事件
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                                Log.e("事件总线",throwable.toString());
                            }
                        });
    }

    /**
     * 只给activity用
     * 现阶段一个Subscription只能订阅一个model事件，如果需要订阅多个model事件就需要定义多个Subscription
     * */
    public static Subscription viewSubscription(@NonNull Context context,
                                            @Nullable final String[] events){
        final ResponseNode responseNode = HandlerFactory.getHandler(context,EventMap.getViewMap(events,context));
        final Map<String, String> modelMap = EventMap.getServiceMap(context);
        return getDefault().toObserverable(RxBusEvent.class)
                .filter(new Func1<RxBusEvent, Boolean>() {
                    @Override
                    public Boolean call(RxBusEvent rxBusEvent) {
                        if(null == events){
                            //没有过滤条件
                            return true;
                        }
                        for(String event : events){
                            if(modelMap.containsKey(event)){
                                if(rxBusEvent.getTag().equals(modelMap.get(event))){
                                    return true;//找到了event对应的model名字
                                }
                            }
                        }
//                        if(modelMap.containsKey(event)){
//                            if(rxBusEvent.getTag().equals(modelMap.get(event))){
//                                return true;//找到了event对应的model名字
//                            }
//                        }
                        return false;
                    }
                })
                .subscribe(new Action1<RxBusEvent>() {
                               @Override
                               public void call(final RxBusEvent rxBusEvent) {
                                   responseNode.operator(rxBusEvent);//开启职责链处理事件
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                // TODO: 处理异常
                                Log.e("事件总线",throwable.toString());
                            }
                        });
    }
}
