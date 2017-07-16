package com.yakshanidhi.art.network;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yakshanidhi.art.BuildConfig;
import com.yakshanidhi.art.Yakshanidhi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ganesh on 16/7/17.
 */

public class APIClient {

    private static Retrofit retrofit = null;
    static CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(Yakshanidhi.getInstance()));
    //Network interceptor for logging
    static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .cookieJar(cookieJar)
            //here we can add Interceptor for dynamical adding headers
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder().addHeader("Accept", "application/json").build();
                    return chain.proceed(request);
                }
            })
            //here we adding Interceptor for full level logging
            .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();


    /**
     * Method to get retrofit client instance. Based on the build, creating instance with/without interceptor
     * @return static retrofit client instance
     */
    public static Retrofit getClient() {
        if (retrofit == null) {
            if (BuildConfig.DEBUG_MODE) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(com.yakshanidhi.art.BuildConfig.BASE_URL)
                        .client(httpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
            } else {
                retrofit = new Retrofit.Builder()
                        .baseUrl(com.yakshanidhi.art.BuildConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }

    /**
     * @return APIInterface retrofit client
     */
    public static APIInterface create() {
        return getClient().create(APIInterface.class);
    }
}
