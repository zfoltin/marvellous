package com.zedeff.marvellous.api;

import com.zedeff.marvellous.api.models.ComicDataWrapper;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface MarvelApi {
    @GET("comics")
    Single<ComicDataWrapper> getComics();
}
