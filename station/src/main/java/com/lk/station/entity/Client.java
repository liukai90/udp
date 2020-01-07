package com.lk.station.entity;


public class Client {

    private String name;

    private String mac;

    private Integer type;

    private String host;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", type=" + type +
                ", host='" + host + '\'' +
                '}';
    }
}
