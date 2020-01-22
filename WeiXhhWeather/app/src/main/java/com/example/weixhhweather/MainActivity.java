package com.example.weixhhweather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weixhhweather.Gson.Forecast;
import com.example.weixhhweather.Gson.WeatherResponse;
import com.example.weixhhweather.Gson.Yesterday;
import com.example.weixhhweather.Utils.DataUtil;
import com.example.weixhhweather.Utils.ProgressDialogUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// http://wthrcdn.etouch.cn/weather_mini?city=深圳
// http://wthrcdn.etouch.cn/weather_mini?citykey=101280601
//{"data":{
//    "yesterday":{"date":"20日星期一","high":"高温 20℃","fx":"无持续风向","low":"低温 14℃","fl":"<![CDATA[<3级]]>","type":"多云"}
//    ,"city":"深圳",
//        "forecast":[
//                {"date":"21日星期二","high":"高温 23℃","fengli":"<![CDATA[<3级]]>","low":"低温 17℃","fengxiang":"无持续风向","type":"阴"},
//        {"date":"22日星期三","high":"高温 24℃","fengli":"<![CDATA[<3级]]>","low":"低温 19℃","fengxiang":"无持续风向","type":"多云"},
//        {"date":"23日星期四","high":"高温 24℃","fengli":"<![CDATA[<3级]]>","low":"低温 19℃","fengxiang":"无持续风向","type":"雾"},
//        {"date":"24日星期五","high":"高温 26℃","fengli":"<![CDATA[<3级]]>","low":"低温 18℃","fengxiang":"无持续风向","type":"多云"},
//        {"date":"25日星期六","high":"高温 23℃","fengli":"<![CDATA[<3级]]>","low":"低温 15℃","fengxiang":"无持续风向","type":"小雨"}
//        ],
//        "ganmao":"各项气象条件适宜，无明显降温过程，发生感冒机率较低。","wendu":"20"},
//        "status":1000,"desc":"OK"}
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView tvTitleName; // 城市名称
    private TextView tvNowDate; // 今天的日期
    private TextView tvNowTemp; // 今天的温度
    private TextView tvNowType; // 今天的天气类型
    private TextView tvNowWind; // 今天的风向
    private TextView tvNowGanmao; // 今天的提示
    private Button SwitchCity; // 切换其他城市
    private ListView lvForecast ; // 预报ListView
    private List<ForecastData> dataList = new ArrayList<>(); // 预报列表
    private ForecastAdapter adapter; // 预报适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitleName = findViewById(R.id.tv_title_name);
        tvNowDate = findViewById(R.id.tv_now_date);
        tvNowTemp = findViewById(R.id.tv_now_temp);
        tvNowType = findViewById(R.id.tv_now_type);
        tvNowWind = findViewById(R.id.tv_now_wind);
        tvNowGanmao = findViewById(R.id.tv_now_ganmao);
        SwitchCity = findViewById(R.id.btn_switch_city);
        lvForecast = findViewById(R.id.lv_forecast_content);
        GetDataAndShowData("深圳");

        // 选择其他城市
        SwitchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1001 && resultCode == 1002) {
            String cityName = data.getStringExtra("cityName");
            GetDataAndShowData(cityName);
        }
    }

    private void GetDataAndShowData(String cityName) {
        ProgressDialogUtil.showProgressDialog(MainActivity.this); // 加载框
        String address = "http://wthrcdn.etouch.cn/weather_mini?city="+cityName;
        DataUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WeatherResponse weatherData = DataUtil.getWeatherData(response.body().string());
                            String cityName = weatherData.getData().getCity(); // 城市名称
                            tvTitleName.setText(cityName);
                            String wendu = weatherData.getData().getWendu(); // 今天的温度
                            tvNowTemp.setText(wendu+"℃");
                            String ganmao = weatherData.getData().getGanmao(); // 今天的提示
                            tvNowGanmao.setText(ganmao);
                            List<Forecast> forecast = weatherData.getData().getForecast(); // 天气信息
                            ForecastData nowData = setNowWeatherData(forecast); // 今天的天气信息

                            // 设置其他日期的信息
                            // 昨天
                            Yesterday yesterday = weatherData.getData().getYesterday();
                            ForecastData yesterdayData = setYesterdayWeatherData(yesterday);
                            // 未来几天
                            ForecastData futureOne = setOtherDayWeatherData(forecast.get(1));
                            ForecastData futureTwo = setOtherDayWeatherData(forecast.get(2));
                            ForecastData futureThree = setOtherDayWeatherData(forecast.get(3));
                            ForecastData futureFour = setOtherDayWeatherData(forecast.get(4));
                            if(dataList.size()!=0) {
                                dataList.clear(); // 清除请一次的数据
                            }
                            dataList.add(yesterdayData);
                            dataList.add(nowData);
                            dataList.add(futureOne);dataList.add(futureTwo);dataList.add(futureThree);dataList.add(futureFour);
                            adapter = new ForecastAdapter(dataList);
                            lvForecast.setAdapter(adapter);

                            ProgressDialogUtil.dismiss(); // 消失加载框
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "获得天气出错了" + e);
            }
        });
    }

    /**
     * 设置今天的天气信息，返回一个ForecastData，用于在预告中显示
     * @param forecast
     * @return
     */
    private ForecastData setNowWeatherData(List<Forecast> forecast) {
        Forecast forecastNow = forecast.get(0); // 今天的天气信息
        String nowDate = forecastNow.getDate(); // 今天的日期
        tvNowDate.setText("今天-"+nowDate);
        String nowTemp = forecastNow.getHigh(); // 今天的最高温度
        String nowLow = forecastNow.getLow(); // 今天的最低温度
        String nowType = forecastNow.getType(); // 今天的天气类型
        tvNowType.setText(nowType);
        String nowWind = forecastNow.getFengxiang(); // 今天的风向
        tvNowWind.setText(nowWind);

        ForecastData nowData = new ForecastData(nowDate, nowTemp.substring(2), nowLow.substring(2), nowType); // 今天的天气
        return nowData;
    }

    /**
     * 设置昨天的信息
     * @param yesterday
     * @return
     */
    private ForecastData setYesterdayWeatherData(Yesterday yesterday) {
        String date = yesterday.getDate(); // 昨天的日期
        String high = yesterday.getHigh(); // 昨天的最高温度
        String low = yesterday.getLow(); // 昨天的最低温度
        String type = yesterday.getType(); // 昨天的天气类型
        ForecastData yesterdayData = new ForecastData(date, high.substring(2), low.substring(2), type); // 昨天的天气
        return yesterdayData;
    }

    /**
     * 未来几天的天气
     * @param forecast
     * @return
     */
    private ForecastData setOtherDayWeatherData(Forecast forecast) {
        String date = forecast.getDate(); // 未来几天的日期
        String high = forecast.getHigh(); // 未来几天的最高温度
        String low = forecast.getLow(); // 未来几天的最低温度
        String type = forecast.getType(); // 未来几天的天气类型
        ForecastData otherDayData = new ForecastData(date, high.substring(2), low.substring(2), type); // 未来几天的天气
        return otherDayData;
    }

}
