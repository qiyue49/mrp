package com.sparkit.mrp.repository;

import com.sparkit.mrp.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}
