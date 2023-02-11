package com.example.btrack;

import com.example.btrack.models.Products;
import com.example.btrack.models.Userdetails;
import com.example.btrack.repository.ProductsRepository;
import com.example.btrack.repository.UserRepository;
import com.example.btrack.service.AlertEmailService;
import com.example.btrack.service.ProductService;
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

	@Autowired
	UserRepository userRepository;

	@Autowired
	ProductService productService;

	@Autowired
	AlertEmailService emailService;

	public static void main(String[] args) {
		SpringApplication.run(BtrackApplication.class, args);
	}

	@Scheduled(cron = "0/40 * * * * *")
	public void runDaily() {
		List<Products> products = productsRepository.findAll();
		String daily  = "*/2 * * * *";
		for (Products product : products) {
			product.decreaseHealth();
			productsRepository.save(product);
		}
		sendAlertsDaily();
	}

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

}
