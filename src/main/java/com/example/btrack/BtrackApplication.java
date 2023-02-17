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
import org.springframework.context.annotation.ComponentScan;
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

	@Scheduled(cron = "0 0 0 * * *")
	public void runDaily() {
		List<Products> products = productsRepository.findAll();
		String originalcron  = "0/40 * * * * *";
		for (Products product : products) {
			product.decreaseHealth();
			productsRepository.save(product);
		}
		sendAlertsDaily();
		sendRoutineDaily();
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

}
