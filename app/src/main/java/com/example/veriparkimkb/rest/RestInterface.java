package com.example.veriparkimkb.rest;

import com.example.veriparkimkb.model.HandshakeRequestModel;
import com.example.veriparkimkb.model.HandshakeResponseModel;
import com.example.veriparkimkb.model.StockDetailRequestModel;
import com.example.veriparkimkb.model.StockDetailResponseModel;
import com.example.veriparkimkb.model.StockListRequestModel;
import com.example.veriparkimkb.model.StockListResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestInterface {

    @Headers({"Accept: application/json"})
    @POST("api/handshake/start")
    Call<HandshakeResponseModel> getHandshake(@Body HandshakeRequestModel body);

    @POST("api/stocks/list")
    Call<StockListResponseModel> getStockList(@HeaderMap Map<String, String> header, @Body StockListRequestModel body);

    @POST("api/stocks/detail")
    Call<StockDetailResponseModel> getStockDetail(@Body StockDetailRequestModel body, @Header("X-VP-Authorization") String authHeader);
}
