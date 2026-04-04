package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.QcRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface QcRecordRepository extends JpaRepository<QcRecord, String> {
    Optional<QcRecord> findByTaskId(String taskId);
}
