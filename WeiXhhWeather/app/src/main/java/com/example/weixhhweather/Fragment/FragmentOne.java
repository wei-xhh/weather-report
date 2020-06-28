package com.example.weixhhweather.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weixhhweather.ForecastAdapter;
import com.example.weixhhweather.ForecastData;
import com.example.weixhhweather.Gson.Forecast;
import com.example.weixhhweather.Gson.WeatherResponse;
import com.example.weixhhweather.Gson.Yesterday;
import com.example.weixhhweather.R;
import com.example.weixhhweather.SelectCityActivity;
import com.example.weixhhweather.Utils.DataUtil;
import com.example.weixhhweather.Utils.ProgressDialogUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentOne extends Fragment {
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        tvTitleName = view.findViewById(R.id.tv_title_name);
        tvNowDate = view.findViewById(R.id.tv_now_date);
        tvNowTemp = view.findViewById(R.id.tv_now_temp);
        tvNowType = view.findViewById(R.id.tv_now_type);
        tvNowWind = view.findViewById(R.id.tv_now_wind);
        tvNowGanmao = view.findViewById(R.id.tv_now_ganmao);
        SwitchCity = view.findViewById(R.id.btn_switch_city);
        lvForecast = view.findViewById(R.id.lv_forecast_content);
        SwitchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelectCityActivity.class);
                startActivityForResult(intent, 1001);
            }
        });
        GetDataAndShowData("深圳");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 选择其他城市


    }

    private void GetDataAndShowData(String cityName) {
        ProgressDialogUtil.showProgressDialog(getContext()); // 加载框
        String address = "http://wthrcdn.etouch.cn/weather_mini?city="+cityName;
        DataUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
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
