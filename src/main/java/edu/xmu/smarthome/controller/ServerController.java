package edu.xmu.smarthome.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.xmu.smarthome.entity.ResultInfo;
import edu.xmu.smarthome.server.ServerService;
import edu.xmu.smarthome.util.MqttHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 服务端控制器
 */
@RestController
@RequestMapping("/server")
public class ServerController {
    private final ServerService serverService;

    private final ObjectMapper mapper = new ObjectMapper();

    public ServerController(ServerService serverService){
        this.serverService = serverService;
    }

    /**
     * 发送控制设备的MQTT消息
     * @param paramMap 请求参数
     */
    @RequestMapping("/controlDevice")
    public ResultInfo controlDevice(@RequestBody Map<String, Object> paramMap){
        Integer deviceId = (Integer) paramMap.get("deviceId");
        //Topic规则   smart_home/control/sensor_{deviceId}
        String message = paramMap.get("command").toString();
        serverService.controlDevice(deviceId, message);
        return ResultInfo.success();
    }
}
