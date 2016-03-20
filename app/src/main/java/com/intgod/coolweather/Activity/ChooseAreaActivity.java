package com.intgod.coolweather.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.intgod.coolweather.R;
import com.intgod.coolweather.db.DBManager;
import com.intgod.coolweather.db.WeatherDB;
import com.intgod.coolweather.model.Area;
import com.intgod.coolweather.model.City;
import com.intgod.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;


public class ChooseAreaActivity extends BaseActivity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_AREA = 2;

    private ListView listView;
    private TextView titleText;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private WeatherDB weatherDB;
    private DBManager dBManager;

    /**
     * 省列表
     */
    private List<Province> provinceList;
    /**
     * 市列表
     */
    private List<City> cityList;
    /**
     * 县列表
     */
    private List<Area> areaList;
    /**
     * 选中的省份
     */
    private Province selectedProvince;
    /**
     * 选中的城市
     */
    private City selectedCity;
    /**
     * 当前选中的级别
     */
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        weatherDB = WeatherDB.getInstance(this);
        dBManager = new DBManager(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(position);
                    queryAreas();
                }
            }
        });
        queryProvinces(); // 加载省级数据
    }



    /**
     * 从数据库中查询全国所有的省
     */
    private void queryProvinces() {

        provinceList = weatherDB.loadProvince(dBManager.openDatabase());
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        }
    }

    private void queryCities() {

        cityList = weatherDB.loadCities(dBManager.openDatabase(), selectedProvince.getProvinceName());
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        }
    }

    private void queryAreas() {
        areaList = weatherDB.loadAreas(dBManager.openDatabase(), selectedCity.getCityName());
        if (areaList.size() > 0) {
            dataList.clear();
            for (Area area : areaList) {
                dataList.add(area.getAreaName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            currentLevel = LEVEL_AREA;
        }

    }

    @Override
    public void onBackPressed() {
        if (currentLevel == LEVEL_AREA) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        } else {
            finish();
        }
    }
}
