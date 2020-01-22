package com.example.weixhhweather.Utils;

import com.example.weixhhweather.Gson.WeatherResponse;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Data Util
 *  根据路径获得数据JSON格式 sendOkHttpRequest
 *  得到JSON后GSON解析 getWeatherData
 */
public class DataUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static WeatherResponse getWeatherData(String responseJson){
        return new Gson().fromJson(responseJson,WeatherResponse.class);
    }
}
