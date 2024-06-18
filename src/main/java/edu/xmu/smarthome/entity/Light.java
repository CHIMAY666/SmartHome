package edu.xmu.smarthome.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Light extends DeviceData {
    private Integer value;
}
