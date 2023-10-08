package com.demo.redistest.repository;

import com.demo.redistest.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CacheRepository implements IDataCacheRepository {
    private final RedisTemplate<Object, Object> template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public CacheRepository(RedisTemplate<Object, Object> template) {
        this.template = template;
    }


    @Override
    public boolean replace(String key, String fieldKey, List<String> objects) {
        try {
            HashOperations<Object, String, List<String>> hashOps = template.opsForHash();
            hashOps.put(Constant.KEY_TXN, fieldKey, objects);
            return true;
        } catch (Exception e) {
            log.warn("=> Failed to save key " + key + " " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean replace(String key, Map<?, ?> objects) {
        try {
            template.opsForHash().putAll(Constant.KEY_TXN, objects);
            return true;
        } catch (Exception e) {
            log.warn("=> Failed to save key " + key + " " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<String> find(String key, String fieldKey) {
        try {
            HashOperations<Object, String, List<String>> hashOps = template.opsForHash();
            return hashOps.get(key, fieldKey);
        } catch (Exception e) {
            if (e.getMessage() == null) {
                log.debug("=> Unable to find key ");
            } else {
                log.debug("=> Unable to get key " + e.getMessage());
            }
        }
        return null;
    }
}