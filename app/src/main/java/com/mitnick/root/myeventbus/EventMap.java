package com.mitnick.root.myeventbus;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 16-6-23.
 */

public class EventMap {

    public static Map<String, String> getServiceMap(Context context){
        XmlResourceParser xrp = context.getResources().getXml(R.xml.eventbusmodelsmap);
        //解析xml并返回
        Map<String,String> serviceMap = new HashMap<>();
        try{
            String event = "",model = "";
            int eventType = xrp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xrp.getName().equals("bean")) { // 判断开始标签元素是否是bean
                        } else if (xrp.getName().equals("event")) {
                            eventType = xrp.next();
                            event = xrp.getText();
                        } else if (xrp.getName().equals("model")) {
                            eventType = xrp.next();
                            model = xrp.getText();
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        if (xrp.getName().equals("bean")) { // 判断结束标签元素是否是bean
                            serviceMap.put(event, model);
                        }
                        break;
                }
                // 进入下一个元素并触发相应事件
                eventType = xrp.next();
            }
        }catch (Exception e){
            e.printStackTrace();
            return serviceMap;
        }
        return serviceMap;
    }
}
