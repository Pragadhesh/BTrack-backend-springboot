package com.example.btrack.repository;

import com.example.btrack.models.Dailycron;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DailycronRepository extends JpaRepository<Dailycron,Long> {

}
