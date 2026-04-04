package com.sparkit.mrp.controller;

import com.sparkit.mrp.entity.ReconciliationBatch;
import com.sparkit.mrp.entity.ReconciliationDetail;
import com.sparkit.mrp.service.ReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reconciliation")
public class ReconciliationController {

    @Autowired
    private ReconciliationService reconciliationService;

    @PostMapping("/batches")
    public ReconciliationBatch createReconciliationBatch(@RequestBody ReconciliationBatch batch) {
        return reconciliationService.createReconciliationBatch(batch);
    }

    @GetMapping("/batches/{id}")
    public ReconciliationBatch getReconciliationBatchById(@PathVariable String id) {
        return reconciliationService.getReconciliationBatchById(id);
    }

    @PostMapping("/details")
    public ReconciliationDetail createReconciliationDetail(@RequestBody ReconciliationDetail detail) {
        return reconciliationService.createReconciliationDetail(detail);
    }

    @PostMapping("/batches/{id}/detail")
    public void executeDetailReconciliation(@PathVariable String id) {
        reconciliationService.executeDetailReconciliation(id);
    }

    @PostMapping("/batches/{id}/summary")
    public void executeSummaryReconciliation(@PathVariable String id) {
        reconciliationService.executeSummaryReconciliation(id);
    }

    @PostMapping("/batches/{id}/physical")
    public void executePhysicalReconciliation(@PathVariable String id) {
        reconciliationService.executePhysicalReconciliation(id);
    }
}
