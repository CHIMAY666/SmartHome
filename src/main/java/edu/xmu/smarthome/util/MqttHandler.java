package edu.xmu.smarthome.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.xmu.smarthome.entity.Device;
import edu.xmu.smarthome.server.ClientService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MqttHandler {
    private MqttClient client;
    private final ClientService clientService;
    private final ObjectMapper mapper;
    public MqttHandler(ClientService clientService){
        this.clientService = clientService;
        mapper = new ObjectMapper();
    }
    @PostConstruct
    public void init() throws MqttException, JsonProcessingException {
        client = new MqttClient(MqttConfig.IP_ADDRESS, MqttConfig.serverId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        options.setAutomaticReconnect(true);
        options.setMaxInflight(1000);
        client.setCallback(new MqttReceiver(clientService));
        client.connect(options);
        client.subscribe(MqttConfig.sub_topic);
        // 初始化设备
        List<Device> devices = clientService.getAllDevices();
        JsonNode deviceList = mapper.readTree(mapper.writeValueAsString(devices));
        ObjectNode json = mapper.createObjectNode();
        json.put("type", "init");
        json.set("deviceList", deviceList);
        MqttMessage msg = new MqttMessage(json.toString().getBytes());
        msg.setQos(MqttConfig.QOS);
        System.out.println("初始化设备完成.");
        client.publish(MqttConfig.pub_topic, msg);
    }
    @PreDestroy
    public void destroy() throws MqttException {
        //停止设备运行
        MqttMessage msg = new MqttMessage("{\"type\":\"stop\"}".getBytes());
        msg.setQos(MqttConfig.QOS);
        client.publish(MqttConfig.pub_topic, msg);
        //断开连接
        client.disconnect();
        client.close();
    }
    public void publish(String topic, String message) {
        try {
            MqttMessage msg = new MqttMessage(message.getBytes());
            msg.setQos(MqttConfig.QOS);
            System.out.println("publish");
            client.publish(topic, msg);
        }
        catch (MqttException e) {
            System.err.println("发布MQTT消息失败！");
            e.printStackTrace();
        }
    }
}
