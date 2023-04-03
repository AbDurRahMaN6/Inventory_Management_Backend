package com.example.demo.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "devices")
public class Device {

    @Id
    private String id;
    private String serialId;

    private String model;

    private String deviceType;

    private boolean published;

    public Device() {

    }

    public Device(String serialId, String model, String deviceType, boolean published) {
        this.serialId = serialId;
        this.model = model;
        this.deviceType = deviceType;
        this.published = published;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean isPublished) {
        this.published = isPublished;
    }

    @Override
    public String toString() {
        return "Devices [id=" + id + ",serialId=" + serialId + ",  model=" + model + ", deviceType=" + deviceType + ", published=" + published + "]";
    }

}
