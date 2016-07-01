package com.mitnick.root.myeventbus.util.net;

/**
 * Created by root on 16-6-10.
 */

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

public class NetAPIFactory {

    private static NetAPI netAPI = null;
    private static Retrofit retrofit = null;

    public static void netAPIInit(String base_server_ip ){
        if(null == retrofit){
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_server_ip)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if(null == netAPI){
            netAPI = retrofit.create(NetAPI.class);
        }
    }

    public static NetAPI getNetAPIInstance(){
        return netAPI;
    }
}
