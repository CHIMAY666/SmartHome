package edu.xmu.smarthome.mapper;

import edu.xmu.smarthome.entity.Device;

import java.util.List;
import java.util.Map;

public interface DeviceMapper {
    List<Device> getAllDevices();
    Device getDevice(Integer deviceId);
    Integer updateDevice(Device device);
}
