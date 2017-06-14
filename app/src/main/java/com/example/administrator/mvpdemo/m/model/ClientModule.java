package com.example.administrator.mvpdemo.m.model;

import android.app.Application;

import com.example.administrator.mvpdemo.m.http.HttpsUtils;
import com.example.administrator.mvpdemo.m.http.RequestInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/6/12.
 */

@Module
public class ClientModule {

    private static int TIME_OUT = 60_000;

    private final static String CLIENT_PRI_KEY = "client.bks";
    private final static String TRUSTSTORE_PUB_KEY = "server.cer";
    private final static String CLIENT_BKS_PASSWORD = "123456789";

    @Singleton
    @Provides
    OkHttpClient.Builder provideOkhttpBuilder() {
        return new OkHttpClient.Builder();
    }

//    @Singleton
//    @Provides
//    HttpsUtils.SSLParams provideSSLParams(Application context) {
//        InputStream bks = null, cer = null;
//        try {
//            bks = context.getAssets().open(CLIENT_PRI_KEY);
//            cer = context.getAssets().open(TRUSTSTORE_PUB_KEY);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bks.close();
//                cer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return HttpsUtils.getSslSocketFactory(bks, CLIENT_BKS_PASSWORD, cer);
//    }

    @Singleton
    @Provides
    HostnameVerifier provideHostnameVerifier() {
        return new HttpsUtils.UnSafeHostnameVerifier();
    }

    @Singleton
    @Provides
    List<Interceptor> provideInterceptors(Application context) {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(new RequestInterceptor(context));
        return interceptors;
    }

//    @Singleton
//    @Provides       //network拦截器
//    Interceptor provodeInterceptor(){
//        return null;
//    }

    @Singleton
    @Provides
    OkHttpClient provideClient(Application application,
                               OkHttpClient.Builder builder,
//                               Interceptor interceptor,
//                               HttpsUtils.SSLParams sslParams,
                               List<Interceptor> interceptors,
                               HostnameVerifier hostnameVerifier) {
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
//                .addNetworkInterceptor(interceptor)
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(hostnameVerifier);
        for (Interceptor tempInterceptor : interceptors) {
            builder.addInterceptor(tempInterceptor);
        }
        return builder.build();
    }


    // TODO 添加RxErrorHandler

}