package com.example.btrack.repository;


import com.example.btrack.models.Products;
import com.example.btrack.models.Userdetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products, Long> {

    List<Products> findByUserAndModule(Userdetails actualUser, String module);

    List<Products> findByUserAndHealthLessThanOrderByHealthAsc(Userdetails user,int health);

    List<Products> findByUserAndHealthGreaterThan(Userdetails user, int i);
}

