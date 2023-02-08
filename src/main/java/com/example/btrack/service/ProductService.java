package com.example.btrack.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ProductService {

    public ResponseEntity<Object> getProductsByCategory(String module, String category) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream stream = getClass().getResourceAsStream("/data/" + module + "/" + category + ".json");
            if (stream == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            Object json = mapper.readValue(stream, Object.class);
            return ResponseEntity.ok(json);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
