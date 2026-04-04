package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SampleRepository extends JpaRepository<Sample, String> {
    Optional<Sample> findByOrderId(String orderId);
}
