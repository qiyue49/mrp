package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.IntegrationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntegrationLogRepository extends JpaRepository<IntegrationLog, String> {
}
