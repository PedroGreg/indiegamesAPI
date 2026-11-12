package br.com.indiegamesapi.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class IdempotencyService {

    private final Map<String, Long> processedKeys = new ConcurrentHashMap<>();

    public Long findCreatedId(String resource, String idempotencyKey) {
        return processedKeys.get(resource + ":" + idempotencyKey);
    }

    public void register(String resource, String idempotencyKey, Long createdId) {
        processedKeys.put(resource + ":" + idempotencyKey, createdId);
    }
}
