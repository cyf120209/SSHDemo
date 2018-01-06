package com.spring.service;

import com.spring.bean.ShadeGroup;
import com.spring.service.manager.RetrofitManager;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import java.util.List;

public interface IShadeGroupService {

    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_SHORT)
    @GET("groups")
    Observable<List<ShadeGroup>> getShadeGroupList(@Query("pageStartIndex") Integer pageStartIndex,
                                                   @Query("pageSize") Integer pageSize);

    @Headers(RetrofitManager.CACHE_CONTROL_AGE + RetrofitManager.CACHE_STALE_SHORT)
    @GET("groups")
    Observable<List<ShadeGroup>> getShadeGroupList();
}
