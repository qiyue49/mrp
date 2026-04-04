package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.ExternalSystemMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExternalSystemMappingRepository extends JpaRepository<ExternalSystemMapping, String> {
    Optional<ExternalSystemMapping> findBySystemIdAndEntityTypeAndInternalId(String systemId, String entityType, String internalId);
    Optional<ExternalSystemMapping> findBySystemIdAndEntityTypeAndExternalId(String systemId, String entityType, String externalId);
}
