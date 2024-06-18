package edu.xmu.smarthome.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.xmu.smarthome.entity.*;
import edu.xmu.smarthome.mapper.DeviceMapper;
import edu.xmu.smarthome.mapper.HumidityMapper;
import edu.xmu.smarthome.mapper.LightMapper;
import edu.xmu.smarthome.mapper.TemperatureMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    private final DeviceMapper deviceMapper;
    private final TemperatureMapper temperatureMapper;
    private final HumidityMapper humidityMapper;
    private final LightMapper lightMapper;
    private final ObjectMapper mapper;

    public ClientService(DeviceMapper deviceMapper, TemperatureMapper temperatureMapper, HumidityMapper humidityMapper, LightMapper lightMapper){
        this.deviceMapper = deviceMapper;
        this.temperatureMapper = temperatureMapper;
        this.humidityMapper = humidityMapper;
        this.lightMapper = lightMapper;
        mapper = new ObjectMapper();
    }

    public List<Device> getAllDevices() {
        return deviceMapper.getAllDevices();
    }

    public Device getDevice(Integer deviceId){
        return deviceMapper.getDevice(deviceId);
    }

    public List<Temperature> getHistoryTemperatures(Integer deviceId){
        return temperatureMapper.getHistory(deviceId);
    }
    public List<DeviceData> getHistory(Integer deviceId, String deviceType){
        List<DeviceData> list = new ArrayList<>();
        switch (deviceType) {
            case "temperature_sensor" -> list.addAll(temperatureMapper.getHistory(deviceId));
            case "humidity_sensor" -> list.addAll(humidityMapper.getHistory(deviceId));
            case "light_sensor" -> list.addAll(lightMapper.getHistory(deviceId));
        }
        return list;
    }
    /** 处理MQTT消息 */
    public void processMessage(String topic, String msg) {
        try {
            JsonNode json = mapper.readTree(msg);
            String deviceType = json.get("deviceType").asText();
            JsonNode data = json.get("data");
            switch (deviceType){
                case "temperature_sensor" -> {
                    Temperature temperature = new Temperature();
                    temperature.setDeviceId(json.get("deviceId").asInt());
                    temperature.setValue(data.get("temperature").floatValue());
                    temperature.setDate(LocalDateTime.parse(json.get("time").asText(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                    temperatureMapper.insert(temperature);
                }
                case "humidity_sensor" -> {
                    Humidity humidity = new Humidity();
                    humidity.setDeviceId(json.get("deviceId").asInt());
                    humidity.setValue(data.get("humidity").floatValue());
                    humidity.setDate(LocalDateTime.parse(json.get("time").asText(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                    humidityMapper.insert(humidity);
                }
                case "light_sensor" -> {
                    Light light = new Light();
                    light.setDeviceId(json.get("deviceId").asInt());
                    light.setValue(data.get("light").asInt());
                    light.setDate(LocalDateTime.parse(json.get("time").asText(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                    lightMapper.insert(light);
                }
                default -> System.err.format("无法解析的消息，主题：%s, 内容：\n%s\n", topic, msg);
            }
        }
        catch (JsonProcessingException e) {
            System.err.println("转换JSON出错！");
        }
    }
}
