package com.example.demo.storage;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResetCodeStorage {
    private final ConcurrentHashMap<String, ResetCodeEntry> codeStore = new ConcurrentHashMap<>();

    public void storeCode(String email, String code, LocalDateTime expiry) {
        codeStore.put(code, new ResetCodeEntry(email, code, expiry));
    }

    public String validateCode(String code) {
        ResetCodeEntry entry = codeStore.get(code);
        if (entry == null) {
            return null;
        }
        if (LocalDateTime.now().isAfter(entry.getExpiry())) {
            codeStore.remove(code);
            return null;
        }
        String email = entry.getEmail();
        codeStore.remove(code);
        return email;
    }

    @Data
    private static class ResetCodeEntry {
        private final String email;
        private final String code;
        private final LocalDateTime expiry;
    }
}