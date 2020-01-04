package com.simple.auto_queue_mvp.main.service;

import com.simple.auto_queue_mvp.main.MainContract;
import com.simple.auto_queue_mvp.main.model.Comment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainService implements MainContract.Servicable {

    MainServiceApi mService;

    public MainService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/")
                .build();

        mService = retrofit.create(com.simple.auto_queue_mvp.main.service.MainServiceApi.class);
    }

    @Override
    public void persistComment(final MainContract.PersistCallback callback, Comment toPersistComment) {
        // Insert Retrofit / ORM / Whatever here to persist the object. These Frameworks use GSON to serialize simple
        // Java objects - for example to JSON for Retrofit.
        mService.persistComment(1, toPersistComment).enqueue(new Callback<MainServiceResponse>() {
            @Override
            public void onResponse(Call<com.simple.auto_queue_mvp.main.service.MainServiceResponse> call, Response<com.simple.auto_queue_mvp.main.service.MainServiceResponse> response) {
                callback.onSuccess();
            }

            @Override
            public void onFailure(Call<com.simple.auto_queue_mvp.main.service.MainServiceResponse> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
