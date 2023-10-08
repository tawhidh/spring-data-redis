package com.demo.redistest.controller;

import com.demo.redistest.Constant;
import com.demo.redistest.dto.Message;
import com.demo.redistest.service.CacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /*@GetMapping("/strings")
    //@ResponseStatus(HttpStatus.CREATED)
    public Map<String, List<String>> loadDummyData() throws JsonProcessingException {
        return cacheService.loadData();
    }*/
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
    public ResponseEntity<?> updateKey(@RequestBody JsonNode requestBody) throws JsonProcessingException {
        List<String> data = cacheService.updateValueForKey(requestBody);
        return ResponseEntity.ok(data);
    }


    /*@PostMapping("/strings")
    public Object updateStatus(@RequestBody Message message) {
        cacheService.replace(Constant.KEY_TXN, message);
        return cacheService.<Map<String, List<Message>>>find(Constant.KEY_TXN, new TypeReference<>() {});
    }

    @GetMapping("/check-dlr")
    public Object getAll() {
        return cacheService.<Map<String, List<Message>>>find(Constant.KEY_TXN, new TypeReference<>() {});
    }*/
}
