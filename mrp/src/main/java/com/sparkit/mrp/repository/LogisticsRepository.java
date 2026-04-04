package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.Logistics;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LogisticsRepository extends JpaRepository<Logistics, String> {
    Optional<Logistics> findByOrderId(String orderId);
}
