package edu.xmu.smarthome.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.xmu.smarthome.entity.Device;
import edu.xmu.smarthome.mapper.DeviceMapper;
import edu.xmu.smarthome.mapper.HumidityMapper;
import edu.xmu.smarthome.mapper.LightMapper;
import edu.xmu.smarthome.mapper.TemperatureMapper;
import edu.xmu.smarthome.util.MqttConfig;
import edu.xmu.smarthome.util.MqttHandler;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Service
public class ServerService {
    private final MqttHandler mqttHandler;
    private final DeviceMapper deviceMapper;
    private final TemperatureMapper temperatureMapper;
    private final HumidityMapper humidityMapper;
    private final LightMapper lightMapper;
    private final ObjectMapper mapper = new ObjectMapper();

    public ServerService(MqttHandler mqttHandler, DeviceMapper deviceMapper,
                         TemperatureMapper temperatureMapper, HumidityMapper humidityMapper,
                         LightMapper lightMapper){
        this.mqttHandler = mqttHandler;
        this.deviceMapper = deviceMapper;
        this.temperatureMapper = temperatureMapper;
        this.humidityMapper = humidityMapper;
        this.lightMapper = lightMapper;
    }

    public void controlDevice(Integer deviceId, String command) {
        String topic = MqttConfig.pub_topic + "/sensor_" + deviceId;
        mqttHandler.publish(topic, command);
        changeDevice(deviceId, command);
    }

    public void changeDevice(Integer deviceId, String status) {
        Device device = deviceMapper.getDevice(deviceId);
        try{
            ObjectNode oldStatus = (ObjectNode) mapper.readTree(device.getStatus());
            ObjectNode newStatus = (ObjectNode) mapper.readTree(status);
            for (Iterator<String> it = newStatus.fieldNames(); it.hasNext(); ) {
                String name = it.next();
                if(oldStatus.has(name)) oldStatus.set(name, newStatus.get(name));
            }
            device.setStatus(oldStatus.toString());
            deviceMapper.updateDevice(device);
        } catch (JsonProcessingException e){
            System.err.println("解析JSON时出错：");
            e.printStackTrace();
        }
    }
    /**
     * 清除设备历史数据
     * @param deviceId 设备id
     */
    public void clearDeviceHistory(Integer deviceId) {
        Device device = deviceMapper.getDevice(deviceId);
        if(device != null){
            switch (device.getType()){
                case "temperature_sensor" -> temperatureMapper.clearHistory(deviceId);
                case "humidity_sensor" -> humidityMapper.clearHistory(deviceId);
                case "light_sensor" -> lightMapper.clearHistory(deviceId);
            }
        }
    }
    /**
     * 清空所有历史数据
     */
    @PostConstruct
    public void clearAllHistory(){
        temperatureMapper.deleteAll();
        humidityMapper.deleteAll();
        lightMapper.deleteAll();
    }
}
