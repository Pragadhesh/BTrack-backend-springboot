package com.example.btrack;

import com.example.btrack.models.Products;
import com.example.btrack.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class BtrackApplication {

	@Autowired
	ProductsRepository productsRepository;
	public static void main(String[] args) {
		SpringApplication.run(BtrackApplication.class, args);
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void runDaily() {
		List<Products> products = productsRepository.findAll();
		System.out.println(products);
		String daily  = "000***";
		for (Products product : products) {
			product.decreaseHealth();
			productsRepository.save(product);
		}
	}

}
