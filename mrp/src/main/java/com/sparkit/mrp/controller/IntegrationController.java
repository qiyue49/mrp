package com.sparkit.mrp.controller;

import com.sparkit.mrp.entity.ExternalSystem;
import com.sparkit.mrp.entity.ExternalSystemMapping;
import com.sparkit.mrp.service.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/integration")
public class IntegrationController {

    @Autowired
    private IntegrationService integrationService;

    @PostMapping("/systems")
    public ExternalSystem registerExternalSystem(@RequestBody ExternalSystem system) {
        return integrationService.registerExternalSystem(system);
    }

    @GetMapping("/systems/{id}")
    public ExternalSystem getExternalSystemById(@PathVariable String id) {
        return integrationService.getExternalSystemById(id);
    }

    @PostMapping("/mappings")
    public ExternalSystemMapping createExternalSystemMapping(@RequestBody ExternalSystemMapping mapping) {
        return integrationService.createExternalSystemMapping(mapping);
    }

    @GetMapping("/mappings/internal")
    public ExternalSystemMapping getExternalSystemMapping(
            @RequestParam String systemId,
            @RequestParam String entityType,
            @RequestParam String internalId) {
        return integrationService.getExternalSystemMapping(systemId, entityType, internalId);
    }

    @GetMapping("/mappings/external")
    public ExternalSystemMapping getExternalSystemMappingByExternalId(
            @RequestParam String systemId,
            @RequestParam String entityType,
            @RequestParam String externalId) {
        return integrationService.getExternalSystemMappingByExternalId(systemId, entityType, externalId);
    }
}
