package com.sparkit.mrp.controller;

import com.sparkit.mrp.service.MrpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mrp")
public class MrpController {

    @Autowired
    private MrpService mrpService;

    @PostMapping("/calculate")
    public String runMrpCalculation() {
        mrpService.runMrpCalculation();
        return "MRP calculation completed successfully";
    }
}
