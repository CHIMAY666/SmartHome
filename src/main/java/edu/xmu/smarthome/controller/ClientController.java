package edu.xmu.smarthome.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.xmu.smarthome.entity.Device;
import edu.xmu.smarthome.entity.ResultInfo;
import edu.xmu.smarthome.server.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 客户端控制器
 */
@Controller
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ClientController(ClientService service){
        clientService = service;
    }

    /**
     * 查询所有设备
     * @return 设备列表
     */
    @RequestMapping("/queryAllDevices")
    @ResponseBody
    public ResultInfo queryAllDevices(){
        List<Device> devices = clientService.getAllDevices();
        ArrayNode list = mapper.createArrayNode();
        try {
            for (Device device : devices) {
                ObjectNode node = mapper.createObjectNode();
                node.put("id", device.getId());
                node.put("type", device.getType());
                node.set("status", mapper.readTree(device.getStatus()));
                list.add(node);
            }
        } catch (JsonProcessingException e){
            System.err.println("将对象转JSON时出现问题!");
            e.printStackTrace();
        }
        return ResultInfo.success(list);
    }

    /**
     * 根据id查找设备
     * @param deviceId 设备id
     * @return 对应id的设备
     */
    @RequestMapping("/queryDevice/{deviceId}")
    @ResponseBody
    public ResultInfo queryDevice(@PathVariable Integer deviceId){
        Device device = clientService.getDevice(deviceId);
        if(device != null) return ResultInfo.success(device);
        return ResultInfo.fail("设备不存在！");
    }

    /**
     * 获取温度传感器的历史温度数据，最多20条
     * @param deviceId 设备id
     * @return 温度数据列表
     */
    @RequestMapping("/queryHistoryTemperatures/{deviceId}")
    @ResponseBody
    public ResultInfo queryHistoryTemperatures(@PathVariable Integer deviceId){
        return ResultInfo.success(clientService.getHistoryTemperatures(deviceId));
    }

    /**
     * 获取传感器的历史数据，最多20条
     * @return 数据列表
     */
    @RequestMapping("/queryHistory")
    @ResponseBody
    public ResultInfo queryHistory(@RequestBody Map<String, Object> paramMap){
        Integer deviceId = (Integer) paramMap.get("deviceId");
        String deviceType = (String) paramMap.get("deviceType");
        return ResultInfo.success(clientService.getHistory(deviceId, deviceType));
    }

    /**
     * 查看设备详情
     * @param deviceId 设备id
     */
    @RequestMapping("/detail/{deviceId}")
    public String showDetail(@PathVariable Integer deviceId, Model model){
        Device device = clientService.getDevice(deviceId);
        model.addAttribute("deviceId", device.getId());
        model.addAttribute("deviceType", device.getType());
        return "detail";
    }
}
