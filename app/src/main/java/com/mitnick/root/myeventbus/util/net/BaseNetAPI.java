package com.mitnick.root.myeventbus.util.net;


import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by root on 16-5-30.
 */

public interface BaseNetAPI {
//    @GET("login")
//    Observable<Response> login(@Query("loginID") String userID,
//                               @Query("loginPsw") String userPsw,
//                               @Query("dataType") String type);

    @Multipart
    @Headers("{headers}")
    @POST("{url}")
    Observable<ResponseBody> upLoadFile(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("image\"; filename=\"image.jpg") RequestBody avatar);

    @Headers("{headers}")
    @POST("{url}")
    Call<ResponseBody> uploadFiles(
            @Path("url") String url,
            @Path("headers") Map<String, String> headers,
            @Part("filename") String description,
            @PartMap()  Map<String, RequestBody> maps);
}
