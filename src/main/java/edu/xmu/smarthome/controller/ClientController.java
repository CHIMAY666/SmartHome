package edu.xmu.smarthome.controller;

import edu.xmu.smarthome.entity.Device;
import edu.xmu.smarthome.entity.ResultInfo;
import edu.xmu.smarthome.server.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 客户端控制器
 */
@Controller
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

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
        return ResultInfo.success(clientService.getAllDevices());
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
    @RequestMapping("/detail/{deviceId}")
    public String showDetail(@PathVariable String deviceId, Model model){
        model.addAttribute("deviceId", deviceId);
        return "detail";
    }
}
