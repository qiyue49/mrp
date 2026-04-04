package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.QcRecord;
import com.sparkit.mrp.repository.QcRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class QcService {

    @Autowired
    private QcRecordRepository qcRecordRepository;

    @Transactional
    public QcRecord createQcRecord(QcRecord qcRecord) {
        // 生成UUID
        String qcId = UUID.randomUUID().toString();
        qcRecord.setQcId(qcId);
        qcRecord.setVersion(1L);
        qcRecord.setCreatedAt(LocalDateTime.now());
        
        // 保存QC记录
        qcRecordRepository.save(qcRecord);
        log.info("QC record created: {}", qcId);

        return qcRecord;
    }

    public QcRecord getQcRecordById(String qcId) {
        return qcRecordRepository.findById(qcId).orElse(null);
    }

    public QcRecord getQcRecordByTaskId(String taskId) {
        return qcRecordRepository.findByTaskId(taskId).orElse(null);
    }
}
