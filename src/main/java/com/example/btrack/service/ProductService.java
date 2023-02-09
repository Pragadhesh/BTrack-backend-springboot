package com.example.btrack.service;

import com.example.btrack.dto.Product;
import com.example.btrack.models.Products;
import com.example.btrack.models.Userdetails;
import com.example.btrack.repository.ProductsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    UserService userService;

    @Autowired
    ProductsRepository productsRepository;

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

    public ResponseEntity<Object> addProduct(String idToken,Product item)
    {
        try {
            Optional<Userdetails> user = userService.getUser(idToken);
            Userdetails actualUser = user.orElse(null);
            if (actualUser != null)
            {
                ObjectMapper mapper = new ObjectMapper();
                InputStream stream = getClass().getResourceAsStream("/data/" + item.getModule() + "/" + item.getCategory() + ".json");
                List<Product> productList = mapper.readValue(stream, mapper.getTypeFactory().constructCollectionType(List.class, Product.class));

                // Use streams to compare the input parameters with the items in the list
                List<Product> matchingProducts = productList.stream()
                        .filter(product -> product.getName().equals(item.getName()) && product.getDescription().equals(item.getDescription()) &&
                                product.getImage_url().equals(item.getImage_url()) && product.getDamage() == item.getDamage())
                        .collect(Collectors.toList());

                if (matchingProducts.isEmpty())
                {
                    return new ResponseEntity<>("No product matched", HttpStatus.INTERNAL_SERVER_ERROR);
                }
                else
                {
                    Products products = new Products(
                            actualUser,item.getName(),item.getDescription(),item.getImage_url(),
                            item.getModule(),item.getCategory(),item.getHealth(),item.getDamage(),
                            item.getUsage(),item.getDays());
                    Products pd = productsRepository.save(products);
                    return new ResponseEntity<>(pd,HttpStatus.OK);
                }
            }
            else
            {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}