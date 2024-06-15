package edu.xmu.smarthome.util;

public class MqttConfig {
    /** 服务质量 */
    public static final Integer QOS = 2;
    /** 服务器地址 */
    public static final String IP_ADDRESS = "tcp://localhost:1883";
    /** 服务端名称 */
    public static final String serverId = "server";
    /** 发布主题 */
    public static final String pub_topic = "smart_home/control";
    /** 设备消息主题 */
    public static final String sub_topic = "smart_home/device/#";
}
