package com.mitnick.root.myeventbus.util.rxbus.responslink;


import com.mitnick.root.myeventbus.util.rxbus.RxBusEvent;

/**
 * Created by root on 16-5-17.
 */
public interface Handler {
    void operator(RxBusEvent rxBusEvent);
}
