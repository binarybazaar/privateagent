package com.binarybazaar.privateagent.main.http.client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ExchangeApi {

    @GET("/ping")
    Call<ResponseBody> ping();

}
