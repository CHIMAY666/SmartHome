package edu.xmu.smarthome.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Humidity {
    private Integer id;
    private Integer deviceId;
    private Float value;
    private LocalDateTime date;
}
