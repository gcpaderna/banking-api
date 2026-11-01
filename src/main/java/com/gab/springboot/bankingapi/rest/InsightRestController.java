package com.gab.springboot.bankingapi.rest;

import com.gab.springboot.bankingapi.service.InsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InsightRestController {

    private InsightService insightService;

    @Autowired
    public InsightRestController(InsightService insightService) {
        this.insightService = insightService;
    }

    @GetMapping("/accounts/{accountId}/insights")
    public String getInsights(@PathVariable int accountId) {
        return insightService.getInsights(accountId);
    }
}