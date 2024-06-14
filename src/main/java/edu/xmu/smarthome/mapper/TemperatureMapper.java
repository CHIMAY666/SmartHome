package edu.xmu.smarthome.mapper;

import edu.xmu.smarthome.entity.Temperature;

import java.util.List;

public interface TemperatureMapper {
    List<Temperature> getHistoryTemperatures(Integer deviceId);
}
