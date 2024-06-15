package edu.xmu.smarthome.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.xmu.smarthome.entity.Device;
import edu.xmu.smarthome.entity.Humidity;
import edu.xmu.smarthome.entity.Temperature;
import edu.xmu.smarthome.mapper.DeviceMapper;
import edu.xmu.smarthome.mapper.TemperatureMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ClientService {
    private final DeviceMapper deviceMapper;
    private final TemperatureMapper temperatureMapper;
    private final ObjectMapper mapper;

    public ClientService(DeviceMapper deviceMapper, TemperatureMapper temperatureMapper){
        this.deviceMapper = deviceMapper;
        this.temperatureMapper = temperatureMapper;
        mapper = new ObjectMapper();
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
                    insertTemperature(temperature);
                }
                case "humidity_sensor" -> {
                    Humidity humidity = new Humidity();
                    humidity.setDeviceId(json.get("deviceId").asInt());
                    humidity.setValue(data.get("humidity").floatValue());
                    humidity.setDate(LocalDateTime.parse(json.get("time").asText(), DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
                }
                default -> System.err.format("无法解析的消息，主题：%s, 内容：\n%s\n", topic, msg);
            }
        }
        catch (JsonProcessingException e) {
            System.err.println("转换JSON出错！");
        }
    }
    public void insertTemperature(Temperature temperature){
        temperatureMapper.insertTemperature(temperature);
    }
}
