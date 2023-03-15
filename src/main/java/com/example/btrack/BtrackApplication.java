package com.example.btrack;

import com.example.btrack.models.Dailycron;
import com.example.btrack.models.Products;
import com.example.btrack.models.Userdetails;
import com.example.btrack.repository.DailycronRepository;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class BtrackApplication {
	public static void main(String[] args) {

		SpringApplication.run(BtrackApplication.class, args);
	}

}

