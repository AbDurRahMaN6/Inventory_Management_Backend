package com.example.demo.repository;

import com.example.demo.models.Device;
import com.example.demo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface DeviceRepository extends MongoRepository<Device, String> {
    List<Device> findByAvailable(boolean available);

    List<Device> findByModelContaining(String model);

//    List<User>findByUsername(String username);
}
