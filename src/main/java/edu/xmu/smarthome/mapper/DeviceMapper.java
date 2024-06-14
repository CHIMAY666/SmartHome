package edu.xmu.smarthome.mapper;

import edu.xmu.smarthome.entity.Device;

import java.util.List;

public interface DeviceMapper {
    List<Device> getAllDevices();
    Device getDevice(Integer deviceId);
}
