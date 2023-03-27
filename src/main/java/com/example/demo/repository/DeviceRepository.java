package com.example.demo.repository;

import com.example.demo.models.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DeviceRepository extends MongoRepository<Device, String> {
    List<Device> findByPublished(boolean published);

    List<Device> findByModelContaining(String model);
}
