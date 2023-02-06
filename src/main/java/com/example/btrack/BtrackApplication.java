package com.example.btrack;

import com.example.btrack.models.Products;
import com.example.btrack.repository.ProductsRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BtrackApplication {

	ProductsRepository productsRepository;
	public static void main(String[] args) {
		SpringApplication.run(BtrackApplication.class, args);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void runDaily() {
		List<Products> products = productsRepository.findAll();
		for (Products product : products) {
			product.decreaseHealth();
			productsRepository.save(product);
		}
	}

}
