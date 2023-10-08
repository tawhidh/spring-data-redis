package com.demo.redistest.service;

import com.demo.redistest.Constant;
import com.demo.redistest.dto.Message;
import com.demo.redistest.repository.CacheRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CacheService {
    private final CacheRepository cacheRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CacheService(CacheRepository cacheRepository) {
        this.cacheRepository = cacheRepository;
    }

    public Map<String, List<String>> loadData() throws JsonProcessingException {
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        int n = 3, x = 1;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                Message object = Message.builder()
                        .msisdn(String.valueOf(i + 100))
                        .messageId(String.valueOf(j + x))
                        .build();
                String record = this.objectMapper.writeValueAsString(object);

                switch (i) {
                    case 1:
                        list1.add(record);
                        break;
                    case 2:
                        list2.add(record);
                        break;
                    case 3:
                        list3.add(record);
                        break;
                }
                x++;
            }
        }
        Map<String, List<String>> valueMap = new HashMap<>();
        valueMap.put("101", list1);
        valueMap.put("102", list2);
        valueMap.put("103", list3);
        cacheRepository.replace(Constant.KEY_TXN, valueMap);
        return valueMap;
    }

    public List<String> getValueForKey(String key, String fieldKey) {
        List<String> messageList = cacheRepository.find(key, fieldKey);
        log.info("messageList: " + messageList);
        return messageList;
    }

    public List<String> updateValueForKey(JsonNode requestBody) throws JsonProcessingException {
        Message message = objectMapper.convertValue(requestBody, Message.class);
        String msisdn = message.getMsisdn();
        String messageId = message.getMessageId();
        String status = message.getStatus();

        List<String> messageIdList = cacheRepository.find(Constant.KEY_TXN, msisdn);
        if (messageIdList != null) {
            for (int i = 0; i < messageIdList.size(); i++) {
                String msgStr = messageIdList.get(i);
                Message msg = objectMapper.readValue(msgStr, Message.class);
                if (msg.getMessageId().equalsIgnoreCase(messageId)) {
                    msg.setStatus(status);
                    String record = this.objectMapper.writeValueAsString(msg);
                    messageIdList.set(i, record);
                    cacheRepository.replace(Constant.KEY_TXN, msisdn, messageIdList);
                    break;
                }
            }
        }
        return messageIdList;
    }
}
