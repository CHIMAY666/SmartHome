package edu.xmu.smarthome.controller;

import edu.xmu.smarthome.entity.ResultInfo;
import edu.xmu.smarthome.server.ClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService service){
        clientService = service;
    }

    @RequestMapping("/queryAllDevices")
    public ResultInfo queryAllDevices(){
        return ResultInfo.success(clientService.getAllDevices());
    }
}
