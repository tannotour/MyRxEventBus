package com.mitnick.root.myeventbus.util.net;

import retrofit.Response;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by root on 16-5-30.
 */

public interface NetAPI {
    @GET("login")
    Observable<Response> login(@Query("loginID") String userID,
                               @Query("loginPsw") String userPsw,
                               @Query("dataType") String type);
}
