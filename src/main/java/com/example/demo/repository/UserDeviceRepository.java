package com.example.demo.repository;

import com.example.demo.models.UserDevices;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDeviceRepository extends MongoRepository<UserDevices, String> {
}
