package com.mitnick.root.myeventbus.util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 16-6-8.
 * !!!该类已废弃!!!
 */

public class BusEventMap {
    private static ArrayList<String> userMotion = null;//用户发送给busservice需要执行的动作
    private static HashMap<String, String> serviceMap = null;//事件到model的映射
    private static HashMap<String, String> modelMsg = null;//model发送的消息

    public static HashMap<String, String> getServiceMap() {
        if(null == serviceMap){
            serviceMap = new HashMap<>();
        }
        return serviceMap;
    }

    public static void setServiceMap(String event, String modelName) {
        if(null == serviceMap){
            serviceMap = new HashMap<>();
        }
        serviceMap.put(event, modelName);
    }

    public static String getModelMsg(String event){
        if(null != modelMsg){
            if(modelMsg.containsKey(event)){
                return modelMsg.get(event);
            }
        }
        return "error";
    }

    public static HashMap<String, String> getModelMsg() {
        if(null == modelMsg){
            modelMsg = new HashMap<>();
        }
        return modelMsg;
    }

    public static void setModelMsg(String event, String msg){
        if(null == modelMsg){
            modelMsg = new HashMap<>();
        }
        modelMsg.put(event, msg);
    }

    public static void clearModelMsg(String event){
        if(modelMsg.containsKey(event)){
            modelMsg.remove(event);
        }
    }

    public static ArrayList<String> getUserMotion() {
        if(null == userMotion){
            userMotion = new ArrayList<>();
        }
        return userMotion;
    }

    public static void setUserMotion(String motion) {
        if(null == userMotion){
            userMotion = new ArrayList<>();
        }
        if(!userMotion.contains(motion)){
            userMotion.add(motion);
        }
    }
}
