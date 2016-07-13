package com.mitnick.root.myeventbus.util.rxbus.responslink;


import android.content.Context;
import android.util.Log;

import com.mitnick.root.myeventbus.util.model.BusModel;
import com.mitnick.root.myeventbus.util.rxbus.RxBusEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by root on 16-5-17.
 */
public class HandlerFactory {
    /**
     * 得到一个空的职责链处理节点
     * @return responseNode 一个空的职责链处理节点
     * */
    public static ResponseNode getHandler(){
        return new ResponseNode(new DoSomthing(null) {
            @Override
            public boolean dosomthing() {
                return false;
            }
        });
    }

    /**
     * @param c 用于反射方法
     * @param context 传入一个Activity用于回调方法
     * @param events 需要分开处理的事件名称
     * @return responseNode
     * */
    @Deprecated
    public static ResponseNode getHandler(final Class<?> c,
                                          final Context context,
                                          String[] events){
        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        for(final String event : events){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing(event) {
                @Override
                public boolean dosomthing() {
                    try {
                        Method method = c.getMethod(event);
                        method.invoke(context);
                        context.getClass().getMethod(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HandlerFactory",e.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
//        buffer.setHandler(responseNode);//循环职责链
        return responseNode;
    }

    /**
     * @param context 传入一个Activity用于回调方法
     * @param events 需要分开处理的事件名称
     * @return responseNode
     * */
    public static ResponseNode getHandler(final Context context,
                                          String[] events){
        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        for(final String event : events){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing(event) {
                @Override
                public boolean dosomthing() {
                    try {
                        Method method = context.getClass().getMethod(event);
                        method.invoke(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HandlerFactory",e.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
//        buffer.setHandler(responseNode);//循环职责链
        return responseNode;
    }

    /**
     * 自动调用model处理数据,model处理完之后通过事件总线将处理结果返回。去掉了presenter层
     * 这个方法只给service用
     * */
    public static ResponseNode getHandlerAutoDo(final String packageName,
                                                String[] models){

        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        for(final String model : models){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing(model) {
                @Override
                public boolean dosomthing() {
                    StringBuilder packageNameBuffer = new StringBuilder(packageName);
                    packageNameBuffer.append(".")
                            .append(model.substring(0,1).toUpperCase())
                            .append(model.substring(1,model.length()))
                            .append("BusModel");//得到model的路径
                    try {
                        Class<?> modelClass = Class.forName(packageNameBuffer.toString());
                        Method method = modelClass.getMethod(model);//调用model的处理函数
                        method.invoke(modelClass.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HandlerFactory",e.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
//        responseNode.setHandler(responseNode);//循环职责链
        return responseNode;
    }

    /**
     * 自动调用model处理数据,model处理完之后通过事件总线将处理结果返回。去掉了presenter层
     * 这个方法只给service用
     * */
    public static ResponseNode getHandlerAutoDo(final String packageName,
                                                String[] models,
                                                final Map<String, String> modelMap){

        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        for(final String model : models){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing(model) {
                @Override
                public boolean dosomthing() {
                    StringBuilder packageNameBuffer = new StringBuilder(packageName);
                    String buffer = model;
                    if(modelMap.containsKey(model)){
                        buffer = modelMap.get(model);
                    }
//                    packageNameBuffer.append(".")
//                            .append(buffer.substring(0,1).toUpperCase())
//                            .append(buffer.substring(1,buffer.length()))
//                            .append("BusModel");//得到model的路径
                    packageNameBuffer.append(".")
                            .append(buffer);//得到model的路径
                    try {
                        Class<?> modelClass = Class.forName(packageNameBuffer.toString());
                        modelClass.newInstance();
//                        Method method = modelClass.getMethod(buffer);//调用model的处理函数
//                        method.invoke(modelClass.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HandlerFactory",e.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
//        responseNode.setHandler(responseNode);//循环职责链
        return responseNode;
    }

    /**
     * 通过配置文件自动调用相应的model处理数据。去掉了presenter层
     * 这个方法只给bus_service用
     * */
    public static ResponseNode getHandlerAutoDo(final String packageName,
                                                final Map<String, String> modelMap){

        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        String[] models = (String[]) modelMap.keySet().toArray();
        for(final String model : models){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing(model) {
                @Override
                public boolean dosomthing() {
                    StringBuilder packageNameBuffer = new StringBuilder(packageName);
                    packageNameBuffer.append(".")
                            .append(modelMap.get(model));//得到model的路径
                    try {
                        Class<?> modelClass = Class.forName(packageNameBuffer.toString());
                        modelClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HandlerFactory",e.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
        return responseNode;
    }

    /**
     * 通过配置文件自动调用相应的model处理数据。去掉了presenter层
     * 这个方法只给bus_service用
     * */
    public static ResponseNode getHandlerAutoDo( final Map<String, String> modelMap){

        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        Object[] models = modelMap.keySet().toArray();
        for(final Object model : models){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing((String)model) {
                @Override
                public boolean dosomthing() {
                    try {
                        BusModel busModel = null;
                        Class<?> modelClass = Class.forName(modelMap.get((String)model));
                        busModel = (BusModel) modelClass.newInstance();
                        if(null == busModel){
                            throw new Exception("BusModel构造错误！请检查是否存在model！");
                        }
//                        String[] buffer = ((String)modelMap.get((String)model)).split("\\.");
//                        busModel.setModelName(buffer[buffer.length-1]);
                        busModel.setModelName((String)modelMap.get((String)model));
                        busModel.setRxBusEvent(getRxBusEvent());
                        busModel.subscribe(busModel);//初始化Model
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HandlerFactory",e.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
        return responseNode;
    }

    /**
     * @param context 传入一个Activity用于回调方法
     * @param events 需要分开处理的事件名称
     * @param eventMap 事件名称的一个转换，总线消息发送者无需知道接受者要回调哪个函数来实现
     * @return responseNode
     * */
    public static ResponseNode getHandler(final Context context,
                                          String[] events,
                                          final Map<String, String> eventMap){
        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        for(final String event : events){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing(event) {
                @Override
                public boolean dosomthing() {
                    try {
                        String buffer = event;
                        if(eventMap.containsKey(event)){
                            buffer = eventMap.get(event);
                        }
                        Method method = context.getClass().getMethod(buffer);
                        method.invoke(context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("HandlerFactory",e.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
//        buffer.setHandler(responseNode);//循环职责链
        return responseNode;//返回职责链的头节点
    }

    /**
     * @param context 传入一个Activity用于回调方法
     * @param eventMap 事件名称的一个转换，总线消息发送者无需知道接受者要回调哪个函数来实现
     * @return responseNode
     * */
    public static ResponseNode getHandler(final Context context,
                                          final Map<String, String> eventMap){
        ResponseNode responseNode = getHandler();
        ResponseNode buffer = responseNode;
        Object[] events = eventMap.keySet().toArray();
        for(final Object event : events){
            buffer = buffer.setHandler(new ResponseNode(new DoSomthing((String)event) {
                @Override
                public boolean dosomthing() {
                    Method method;
                    String bufferClass = (String)event;
                    if(eventMap.containsKey(event)){
                        bufferClass = eventMap.get(event);
                    }
                    try {
                        method = context.getClass().getMethod(bufferClass,RxBusEvent.class);
                        method.invoke(context,getRxBusEvent());
                    } catch (NoSuchMethodException ee1) {
                        ee1.printStackTrace();
                        Log.e("HandlerFactory",ee1.toString());
                        try {
                            method = context.getClass().getMethod(bufferClass);
                            method.invoke(context);
                        } catch (NoSuchMethodException e1) {
                            e1.printStackTrace();
                            Log.e("HandlerFactory",e1.toString());
                            return filter();
                        } catch (InvocationTargetException e2) {
                            e2.printStackTrace();
                            Log.e("HandlerFactory",e2.toString());
                            return filter();
                        } catch (IllegalAccessException e3) {
                            e3.printStackTrace();
                            Log.e("HandlerFactory",e3.toString());
                            return filter();
                        }
                        return filter();
                    } catch (InvocationTargetException ee2){
                        ee2.printStackTrace();
                        Log.e("HandlerFactory",ee2.toString());
                        return filter();
                    } catch (IllegalAccessException ee3){
                        ee3.printStackTrace();
                        Log.e("HandlerFactory",ee3.toString());
                        return filter();
                    }
                    return filter();
                }
            }));
        }
//        buffer.setHandler(responseNode);//循环职责链
        return responseNode;//返回职责链的头节点
    }

    /**
     * @param context 传入一个Activity用于回调方法
     * @param eventMap 事件名称的一个转换，总线消息发送者无需知道接受者要回调哪个函数来实现
     * @return responseNode
     * */
//    public static ResponseNode getHandler(final Context context,
//                                          final Map<String, Map<String, String> > eventMap){
//        ResponseNode responseNode = getHandler();
//        ResponseNode buffer = responseNode;
//        Object[] events = eventMap.keySet().toArray();
//        for(final Object event : events){
//            buffer = buffer.setHandler(new ResponseNode(new DoSomthing((String)event) {
//                @Override
//                public boolean dosomthing() {
//
//                    eventMap.containsValue(getRxBusEvent().getTag())
//
//                    Method method;
//                    String bufferClass = (String)event;
//                    if(eventMap.containsKey(event)){
//                        bufferClass = eventMap.get(event);
//                    }
//                    try {
//                        method = context.getClass().getMethod(bufferClass,RxBusEvent.class);
//                        method.invoke(context,getRxBusEvent());
//                    } catch (NoSuchMethodException ee1) {
//                        ee1.printStackTrace();
//                        Log.e("HandlerFactory",ee1.toString());
//                        try {
//                            method = context.getClass().getMethod(bufferClass);
//                            method.invoke(context);
//                        } catch (NoSuchMethodException e1) {
//                            e1.printStackTrace();
//                            Log.e("HandlerFactory",e1.toString());
//                            return filter();
//                        } catch (InvocationTargetException e2) {
//                            e2.printStackTrace();
//                            Log.e("HandlerFactory",e2.toString());
//                            return filter();
//                        } catch (IllegalAccessException e3) {
//                            e3.printStackTrace();
//                            Log.e("HandlerFactory",e3.toString());
//                            return filter();
//                        }
//                        return filter();
//                    } catch (InvocationTargetException ee2){
//                        ee2.printStackTrace();
//                        Log.e("HandlerFactory",ee2.toString());
//                        return filter();
//                    } catch (IllegalAccessException ee3){
//                        ee3.printStackTrace();
//                        Log.e("HandlerFactory",ee3.toString());
//                        return filter();
//                    }
//                    return filter();
//                }
//            }));
//        }
////        buffer.setHandler(responseNode);//循环职责链
//        return responseNode;//返回职责链的头节点
//    }

}
