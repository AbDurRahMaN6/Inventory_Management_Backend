package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Document(collection = "userDevices")
public class UserDevices {
    @Id
    public String id;
    public String model;
    public String deviceType;

    public UserDevices() {

    }

    public UserDevices(String model, String deviceType) {
        this.model = model;
        this.deviceType = deviceType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    @Override
    public String toString() {
        return "Devices [id=" + id + ", model=" + model + ", deviceType=" + deviceType +"]";
    }
}
