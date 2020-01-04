package com.simple.auto_queue_mvp.main.service;

import com.simple.auto_queue_mvp.main.model.Comment;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MainServiceApi {

    @PUT("comment/{id}")
    Call<MainServiceResponse> persistComment(@Path("id") int id, @Body Comment comment);
}
