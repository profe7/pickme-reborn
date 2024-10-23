package me.pick.metrodata.utils;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    private ResponseHandler() {
    }
    public static ResponseEntity<Object> generateResponse(Response response) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", response.getMessage());
        map.put("code", response.getCode().value());
        map.put("status", response.getStatus());
        map.put("content", response.getResponseObject());

        return new ResponseEntity<>(map, response.getCode());
    }
}

