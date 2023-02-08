package com.example.btrack.controller;

import com.example.btrack.service.Authenticator;
import com.example.btrack.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    Authenticator authService;

    @Autowired
    ProductService productService;

    @GetMapping("/product/{module}/{category}")
    public ResponseEntity<Object> getProducts(@RequestHeader("Authorization") String authorization,
                                              @PathVariable String category,@PathVariable String module)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return productService.getProductsByCategory(module,category);
        }
    }
}
