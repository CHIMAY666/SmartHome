package edu.xmu.smarthome.server;

import edu.xmu.smarthome.entity.Device;
import edu.xmu.smarthome.entity.Temperature;
import edu.xmu.smarthome.mapper.DeviceMapper;
import edu.xmu.smarthome.mapper.TemperatureMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final DeviceMapper deviceMapper;
    private final TemperatureMapper temperatureMapper;

    public ClientService(DeviceMapper deviceMapper, TemperatureMapper temperatureMapper){
        this.deviceMapper = deviceMapper;
        this.temperatureMapper = temperatureMapper;
    }

    public List<Device> getAllDevices(){
        return deviceMapper.getAllDevices();
    }

    public Device getDevice(Integer deviceId){
        return deviceMapper.getDevice(deviceId);
    }

    public List<Temperature> getHistoryTemperatures(Integer deviceId){
        return temperatureMapper.getHistoryTemperatures(deviceId);
    }
}
