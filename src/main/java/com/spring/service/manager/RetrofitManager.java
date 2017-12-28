package com.spring.service.manager;

import com.spring.bean.ShadeEntity;
import com.spring.service.IShadeService;
import io.reactivex.Observable;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetrofitManager {

    public static final String BASE_URL="http://192.168.20.17:8080/ApiTest/";

    //短缓存有效期为1分钟
    public static final int CACHE_STALE_SHORT = 60;

    public static final String CACHE_CONTROL_AGE="Cache-Control: public, max-age=";

    private static OkHttpClient mOkHttpClient;

    private IShadeService mIShadeService;

    public static RetrofitManager Builder(){
        return new RetrofitManager();
    }

    public RetrofitManager() {
        initOkhttpClient();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mIShadeService=retrofit.create(IShadeService.class);

    }

    private void initOkhttpClient() {
        if(mOkHttpClient==null){
            synchronized (RetrofitManager.class){
                if(mOkHttpClient==null){
                    mOkHttpClient = new OkHttpClient.Builder()
//                            .addInterceptor(mRewriteCacheControlInterceptor)
//                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .retryOnConnectionFailure(true)
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();

                }
            }
        }
    }

    // 云端响应头拦截器，用来配置缓存策略
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            Response originalResponse = chain.proceed(request);
//            if (NetUtil.isNetworkConnected()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
//            } else {
//                return originalResponse.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_LONG)
//                        .removeHeader("Pragma").build();
//            }
        }
    };

    public Observable<List<ShadeEntity>> getShadeList(Integer pageStartIndex,Integer pageSize){
        return mIShadeService.getShadeList(pageStartIndex,pageSize);
    }

    public Observable<List<ShadeEntity>> getShadeList(){
        return mIShadeService.getShadeList();
    }
}
