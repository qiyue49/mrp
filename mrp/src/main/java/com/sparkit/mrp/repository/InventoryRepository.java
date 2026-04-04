package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByMaterialCode(String materialCode);
}
