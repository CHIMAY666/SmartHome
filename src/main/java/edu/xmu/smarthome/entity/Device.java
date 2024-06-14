package edu.xmu.smarthome.entity;

import lombok.Data;

@Data
public class Device {
    private Integer id;
    private String type;
    private String status;
}
