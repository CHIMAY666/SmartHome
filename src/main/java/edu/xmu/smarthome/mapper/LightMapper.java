package edu.xmu.smarthome.mapper;

import edu.xmu.smarthome.entity.Light;

import java.util.List;

public interface LightMapper {
    List<Light> getHistory(Integer deviceId);

    void insert(Light light);

    void clearHistory(Integer deviceId);

    void deleteAll();
}
