package com.example.btrack.controller;

import com.example.btrack.dto.Product;
import com.example.btrack.models.Products;
import com.example.btrack.models.Userdetails;
import com.example.btrack.repository.DailycronRepository;
import com.example.btrack.repository.ProductsRepository;
import com.example.btrack.repository.UserRepository;
import com.example.btrack.service.AlertEmailService;
import com.example.btrack.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoutineController {

    @Autowired
    ProductsRepository productsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductService productService;

    @Autowired
    AlertEmailService emailService;

    public void sendAlertsDaily()
    {
        List<Userdetails> users = userRepository.findAll();
        for (Userdetails user : users)
        {
            List<Products> products = productService.getAlertsforUser(user);
            System.out.println(products);
            if(!products.isEmpty())
            {
                emailService.sendCustomEmailUsingSIB(user.getEmail(),"Alerttemplate",products);
            }
        }
    }

    public void sendRoutineDaily()
    {
        List<Userdetails> users = userRepository.findAll();
        for (Userdetails user : users)
        {
            List<Products> products = productService.getDailyRoutine(user);
            System.out.println(products);
            if(!products.isEmpty())
            {
                emailService.sendRoutineEmailUsingSIB(user.getEmail(),"Routinetemplate",products);
            }
        }
    }

    @PostMapping("/sendemail")
    public ResponseEntity<Object> addProduct(@RequestHeader("initiator") String initiator)
    {
        if (initiator.equals("lambda"))
        {
            List<Products> products = productsRepository.findAll();
            for (Products product : products) {
                product.decreaseHealth();
                productsRepository.save(product);
            }
            sendAlertsDaily();
            sendRoutineDaily();
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>("Your token is invalid or has expired", HttpStatus.UNAUTHORIZED);
        }
    }
}
