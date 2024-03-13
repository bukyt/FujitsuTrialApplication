package com.trialTask.deliveryapp.DAO;

import com.trialTask.deliveryapp.entity.WeatherData;

public interface dao {
    void insertData(String name, int WMOCODE, float wind, float air, String phenomenon);
    String selectAll();
    WeatherData selectLatestByLocation(String locationName);
    void close();
}
