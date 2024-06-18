package edu.xmu.smarthome.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceData {
    protected Integer id;
    protected Integer deviceId;
    protected LocalDateTime date;
}
