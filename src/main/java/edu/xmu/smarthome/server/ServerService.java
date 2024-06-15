package edu.xmu.smarthome.server;

import edu.xmu.smarthome.util.MqttConfig;
import edu.xmu.smarthome.util.MqttHandler;
import org.springframework.stereotype.Service;

@Service
public class ServerService {
    private final MqttHandler mqttHandler;

    public ServerService(MqttHandler mqttHandler){
        this.mqttHandler = mqttHandler;
    }
    public void controlDevice(Integer deviceId, String command){
        String topic = MqttConfig.pub_topic + "/sensor_" + deviceId;
        mqttHandler.publish(topic, command);
    }
}
