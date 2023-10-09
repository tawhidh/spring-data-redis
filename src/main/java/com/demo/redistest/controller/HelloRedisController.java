package com.demo.redistest.controller;

import com.demo.redistest.Constant;
import com.demo.redistest.service.CacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/redis")
public class HelloRedisController {
    @Autowired
    CacheService cacheService;

    @GetMapping("/load")
    public ResponseEntity<Map<String, List<String>>> loadDummyData() throws JsonProcessingException {
        Map<String, List<String>> data = cacheService.loadData();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/key")
    public ResponseEntity<List<String>> getKey(@RequestParam String msisdn) throws JsonProcessingException {
        List<String> data = cacheService.getValueForKey(Constant.KEY_TXN, msisdn);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/update")
    public ResponseEntity<List<String>> updateKey(@RequestBody JsonNode requestBody) throws JsonProcessingException {
        List<String> data = cacheService.updateValueForKey(requestBody);
        return ResponseEntity.ok(data);
    }
}
