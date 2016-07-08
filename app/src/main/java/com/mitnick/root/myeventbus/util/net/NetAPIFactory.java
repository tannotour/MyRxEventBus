package com.mitnick.root.myeventbus.util.net;

/**
 * Created by root on 16-6-10.
 */

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetAPIFactory {

    private static NetAPI netAPI = null;
    private static Retrofit retrofit = null;

    public static void netAPIInit(String base_server_ip ){
        if(null == retrofit){

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //设置超时
            builder.connectTimeout(15, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);
            setInterceptor(builder);

            retrofit = new Retrofit.Builder()
                    .baseUrl(base_server_ip)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        if(null == netAPI){
            netAPI = retrofit.create(NetAPI.class);
        }
    }

    private static void setInterceptor(OkHttpClient.Builder builder){
        //公共参数
        Interceptor addQueryParameterInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request request;
                String method = originalRequest.method();
                Headers headers = originalRequest.headers();
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        //在这里添加公共参数
                        .addQueryParameter("platform", "android")
                        .addQueryParameter("version", "1.0.0")
                        .addQueryParameter("token","")
                        .build();
                request = originalRequest.newBuilder().url(modifiedUrl).build();
                return chain.proceed(request);
            }
        };
        //设置公共参数
        builder.addInterceptor(addQueryParameterInterceptor);
        //请求头
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .header("AppType", "TPOS")
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                        .method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        //设置请求头
        builder.addInterceptor(headerInterceptor );
    }

    public static NetAPI getNetAPIInstance(){
        return netAPI;
    }
}
