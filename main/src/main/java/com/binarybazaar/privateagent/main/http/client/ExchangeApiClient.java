package com.binarybazaar.privateagent.main.http.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExchangeApiClient {

    private static final String API_BASE_URL = "https://api.exchange.com/";

    private final Retrofit retrofit;

    private final ExchangeApi exchangeApi;

    public ExchangeApiClient() {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();

        this.retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(httpClient)
                .build();

        this.exchangeApi = retrofit.create(ExchangeApi.class);

    }

    public boolean ping() throws IOException {
        Call<ResponseBody> ping = this.exchangeApi.ping();

        Response<ResponseBody> response = ping.execute();

        if (response.isSuccessful()) {
            return true;
        }

        return false;
    }

}
