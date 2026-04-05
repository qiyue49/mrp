package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.Material;
import com.sparkit.mrp.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Transactional(readOnly = true)
    public Page<Material> getMaterials(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Material> getMaterialById(Long id) {
        return materialRepository.findById(id);
    }

    @Transactional
    public Material createMaterial(Material material) {
        validateMaterial(material);
        return materialRepository.save(material);
    }

    @Transactional
    public Material updateMaterial(Long id, Material material) {
        validateMaterial(material);
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Material not found with id: " + id));
        
        // Update fields
        existingMaterial.setMaterialCode(material.getMaterialCode());
        existingMaterial.setMaterialName(material.getMaterialName());
        existingMaterial.setCategory(material.getCategory());
        existingMaterial.setUnit(material.getUnit());
        existingMaterial.setPrice(material.getPrice());
        existingMaterial.setLeadTime(material.getLeadTime());
        existingMaterial.setAlternativeMaterial(material.getAlternativeMaterial());
        existingMaterial.setStatus(material.getStatus());
        
        return materialRepository.save(existingMaterial);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new IllegalArgumentException("Material not found with id: " + id);
        }
        materialRepository.deleteById(id);
    }

    private void validateMaterial(Material material) {
        if (material.getMaterialCode() == null || material.getMaterialCode().isEmpty()) {
            throw new IllegalArgumentException("Material code is required");
        }
        if (material.getMaterialName() == null || material.getMaterialName().isEmpty()) {
            throw new IllegalArgumentException("Material name is required");
        }
        if (material.getCategory() == null || material.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }
        if (material.getUnit() == null || material.getUnit().isEmpty()) {
            throw new IllegalArgumentException("Unit is required");
        }
        if (material.getPrice() == null || material.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (material.getLeadTime() == null || material.getLeadTime() < 0) {
            throw new IllegalArgumentException("Lead time must be non-negative");
        }
        if (material.getStatus() == null || material.getStatus().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }
    }
}
