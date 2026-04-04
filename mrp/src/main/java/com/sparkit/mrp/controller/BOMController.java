package com.sparkit.mrp.controller;

import com.sparkit.mrp.entity.BOM;
import com.sparkit.mrp.repository.BOMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bom")
public class BOMController {

    @Autowired
    private BOMRepository bomRepository;

    @PostMapping
    public BOM createBOM(@RequestBody BOM bom) {
        return bomRepository.save(bom);
    }

    @GetMapping
    public List<BOM> getAllBOMs() {
        return bomRepository.findAll();
    }

    @GetMapping("/product/{productCode}")
    public List<BOM> getBOMByProductCode(@PathVariable String productCode) {
        return bomRepository.findByProductCode(productCode);
    }

    @PutMapping("/{id}")
    public BOM updateBOM(@PathVariable Long id, @RequestBody BOM bom) {
        bom.setId(id);
        return bomRepository.save(bom);
    }

    @DeleteMapping("/{id}")
    public void deleteBOM(@PathVariable Long id) {
        bomRepository.deleteById(id);
    }
}
