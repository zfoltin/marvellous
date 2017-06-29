package com.zedeff.marvellous.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zedeff.marvellous.BuildConfig;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://gateway.marvel.com/v1/public/";
    private static final String MARVEL_API_KEY = "9e105f157db909233ebf4dedba902db1";
    private static final String MARVEL_HASH = "bd9bd6586d58c67006ff28c576cdb802";

    private Context context;

    public ApiClient(Context context) {
        this.context = context;
    }

    public MarvelApi createMarvelApi() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(createGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(createOkHttpClient())
                .build()
                .create(MarvelApi.class);
    }

    private Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        return gsonBuilder.create();
    }

    private OkHttpClient createOkHttpClient() {
        Interceptor hasNetworkInterceptor = chain -> {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
            if (isConnected) {
                return chain.proceed(chain.request());
            } else {
                throw new NoNetworkException();
            }
        };

        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("ts", "1")
                    .addQueryParameter("apikey", MARVEL_API_KEY)
                    .addQueryParameter("hash", MARVEL_HASH)
                    .addQueryParameter("limit", "100")
                    .build();

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        okhttp3.Cache cache = new okhttp3.Cache(context.getCacheDir(), 10 * 1024 * 1024);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().cache(cache);

        return builder
                .addInterceptor(hasNetworkInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor).build();
    }

    public class NoNetworkException extends IOException {
    }
}
