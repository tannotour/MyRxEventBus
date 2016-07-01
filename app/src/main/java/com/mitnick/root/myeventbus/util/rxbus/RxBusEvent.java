package com.mitnick.root.myeventbus.util.rxbus;

/**
 * Created by root on 16-4-12.
 */
public class RxBusEvent {
    private String tag;
    private String event;
    private Object object;

    public RxBusEvent(){}

    public RxBusEvent(String tag, String event, Object object){
        this.tag = tag;
        this.event = event;
        this.object = object;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
