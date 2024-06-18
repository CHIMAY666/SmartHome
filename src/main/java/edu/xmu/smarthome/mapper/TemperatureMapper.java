package edu.xmu.smarthome.mapper;

import edu.xmu.smarthome.entity.Temperature;

import java.util.List;

public interface TemperatureMapper {
    List<Temperature> getHistory(Integer deviceId);

    void insert(Temperature temperature);

    void clearHistory(Integer deviceId);

    void deleteAll();
}
