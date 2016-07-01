package com.mitnick.root.myeventbus.util.tcp;


import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.reactivex.netty.channel.Connection;
import io.reactivex.netty.protocol.tcp.client.TcpClient;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func0;

/**
 * Created by root on 16-5-25.
 */

public class RxNetty {

    private Connection<String, String> mConnection;

    public RxNetty(){

    }

    /**
     * 连接tcp服务器
     * @param ip 服务器的ip地址
     * @param port 服务器的端口
     * @return Observable<Boolean> 返回一个可观察者
     * */
    public Observable<Boolean> connect(final String ip,final int port){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                TcpClient.newClient(ip, port).<String, String>addChannelHandlerLast("decoder",
                        new Func0<ChannelHandler>() {
                            @Override
                            public ChannelHandler call() {
                                return new StringDecoder();
                            }
                        }).<String, String>addChannelHandlerLast("encoder",
                        new Func0<ChannelHandler>() {
                            @Override
                            public ChannelHandler call() {
                                return new StringEncoder();
                            }
                        }).createConnectionRequest().subscribe(new Observer<Connection<String, String>>() {
                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(Connection<String, String> objectObjectConnection) {
                        mConnection = objectObjectConnection;
                        subscriber.onNext(true);
                    }
                });
            }
        });
    }

    /**
     * 接受消息
     * @return Observable<String> 返回接受到的消息
     * */
    public Observable<String> receive() {
        if (mConnection != null) {
            return mConnection.getInput();
        }
        return null;
    }

    /**
     * 发送消息
     * @param s 要发送的消息
     * */
    public Observable<Void> send(String s) {
        return mConnection.writeString(Observable.just(s));
    }
}
