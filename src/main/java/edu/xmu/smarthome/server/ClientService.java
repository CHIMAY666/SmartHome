package edu.xmu.smarthome.server;

import edu.xmu.smarthome.entity.Device;
import edu.xmu.smarthome.mapper.DeviceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final DeviceMapper deviceMapper;

    public ClientService(DeviceMapper mapper){
        deviceMapper = mapper;
    }

    public List<Device> getAllDevices(){
        return deviceMapper.getAllDevices();
    }
}
