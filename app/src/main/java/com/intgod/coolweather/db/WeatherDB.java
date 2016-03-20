package com.intgod.coolweather.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.intgod.coolweather.model.Area;
import com.intgod.coolweather.model.City;
import com.intgod.coolweather.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2016/3/8.
 * 数据库操作封装
 */
public class WeatherDB {


    private static WeatherDB weatherDB;
    private Context context;

    public WeatherDB(Context context) {
        this.context = context;
    }

    public synchronized static WeatherDB getInstance(Context context) {
        if (weatherDB == null) {
            weatherDB = new WeatherDB(context);
        }
        return weatherDB;
    }


    /*
    *从数据库读取全国所有省份信息
     */
    public List<Province> loadProvince(SQLiteDatabase db) {
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select distinct province_name from weathers", null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
//                province.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(province);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /*
    * 从数据库读取某省下所有的城市信息
     */

    public List<City> loadCities(SQLiteDatabase db, String provinceName) {
        List<City> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select distinct city_name from weathers where province_name = ?", new String[]{provinceName});
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
//                city.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
//                city.setProvinceId(provinceId);
                list.add(city);

            }while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /*
    * 从数据库读取某城市下所有的县信息
     */

    public List<Area> loadAreas(SQLiteDatabase db, String cityName) {
        List<Area> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("select distinct area_name from weathers where city_name = ?", new String[]{cityName});
        if (cursor.moveToFirst()) {
            do {
                Area area = new Area();
                area.setAreaName(cursor.getString(cursor.getColumnIndex("area_name")));
//                area.setAreaId(cursor.getInt(cursor.getColumnIndex("area_id")));
//                area.setCityId(cityId);
                list.add(area);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
}
