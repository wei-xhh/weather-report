package com.example.weixhhweather;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.weixhhweather.Fragment.FragmentOne;
import com.example.weixhhweather.Fragment.FragmentThree;
import com.example.weixhhweather.Fragment.FragmentTwo;

import java.util.ArrayList;
import java.util.List;

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
public class WeatherActivity extends AppCompatActivity {
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
    private ViewPager vpWeather;
    private FragmentAdapter fragmentAdapter;
    private List<Fragment> vpList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);
        vpWeather = findViewById(R.id.vp_weather);
        vpList.add(new FragmentOne());vpList.add(new FragmentTwo());vpList.add(new FragmentThree());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), vpList);
        vpWeather.setAdapter(fragmentAdapter);
    }

}
