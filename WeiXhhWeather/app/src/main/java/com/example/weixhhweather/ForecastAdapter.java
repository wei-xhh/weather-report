package com.example.weixhhweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ForecastAdapter extends BaseAdapter {
    private List<ForecastData> dataList;

    public ForecastAdapter(List<ForecastData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ForecastData forecastData = dataList.get(i); // 天气
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.forecast_list_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.tvDate = view.findViewById(R.id.tv_date);
            viewHolder.tvHigh = view.findViewById(R.id.tv_high);
            viewHolder.tvLow = view.findViewById(R.id.tv_low);
            viewHolder.tvType = view.findViewById(R.id.tv_type);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.tvDate.setText(forecastData.getDate());
        viewHolder.tvHigh.setText(forecastData.getHigh());
        viewHolder.tvLow.setText(forecastData.getLow());
        viewHolder.tvType.setText(forecastData.getType());

        return view;
    }
    class ViewHolder{
        TextView tvDate;
        TextView tvHigh;
        TextView tvLow;
        TextView tvType;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
