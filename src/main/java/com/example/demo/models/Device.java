package com.example.demo.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "devices")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Device {

    @Id
    private String id;

    @Indexed(unique = true)
    private String serialId;

    private String model;

    private String username;

    private Status status;

    private DeviceStatus deviceStatus;
    private DeviceType deviceType;
    private OperatingSystem operatingSystem;

    public enum Status {
        ASSIGNED, NOT_ASSIGNED, DAMAGED, OUTDATED, DELETED
    }

    public enum DeviceStatus {
         USED, NEW

    }

    public enum DeviceType {
        LAPTOP, TABLET, DESKTOP, SMARTPHONE, IPAD, CPU
    }

    public enum OperatingSystem {
        WINDOWS, LINUX, IOS, ANDROID
    }

}
