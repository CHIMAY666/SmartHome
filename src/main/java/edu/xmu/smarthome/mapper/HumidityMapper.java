package edu.xmu.smarthome.mapper;

import edu.xmu.smarthome.entity.Humidity;

import java.util.List;

public interface HumidityMapper {
    List<Humidity> getHistory(Integer deviceId);

    void insert(Humidity humidity);

    void clearHistory(Integer deviceId);

    void deleteAll();
}
