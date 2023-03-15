package com.example.btrack.controller;

import com.example.btrack.dto.Product;
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

    @GetMapping("/addproduct/{module}/{category}")
    public ResponseEntity<Object> addProducts(@RequestHeader("authorization") String authorization, @PathVariable String category,@PathVariable String module)
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

    @PostMapping("/product")
    public ResponseEntity<Object> addProduct(@RequestHeader("authorization") String authorization,
                                             @RequestBody Product product)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return productService.addProduct(idToken,product);
        }

    }


    @PutMapping("/product")
    public ResponseEntity<Object> updateProduct(@RequestHeader("authorization") String authorization,
                                             @RequestBody Product product)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return productService.updateProduct(idToken,product);
        }

    }


    @DeleteMapping("/product")
    public ResponseEntity<Object> deleteProduct(@RequestHeader("authorization") String authorization,
                                                @RequestBody Product product) {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result) {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        } else {
            return productService.deleteProduct(idToken, product);
        }
    }

    @GetMapping("/product/{module}")
    public ResponseEntity<Object> getProducts(@RequestHeader("authorization") String authorization,@PathVariable String module)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return productService.getProduct(idToken,module);
        }
    }

    @GetMapping("/alerts")
    public ResponseEntity<Object> getAlerts(@RequestHeader("authorization") String authorization)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return productService.getAlerts(idToken);
        }
    }

    @GetMapping("/recommendation")
    public ResponseEntity<Object> getRecommendation(@RequestHeader("authorization") String authorization)
    {
        String idToken = authorization.replace("Bearer ", "");
        Boolean result = authService.isTokenExpired(idToken);
        if (result)
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
        else
        {
            return productService.getDailyRecommendation(idToken);
        }
    }


}
