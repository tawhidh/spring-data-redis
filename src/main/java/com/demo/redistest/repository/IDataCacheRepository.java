package com.demo.redistest.repository;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

public interface IDataCacheRepository {
    boolean replace(String key, String fieldKey, List<String> objects);

    boolean replace(String key, Map<?, ?> object);

    List<String> find(String key, String fieldKey);
}
