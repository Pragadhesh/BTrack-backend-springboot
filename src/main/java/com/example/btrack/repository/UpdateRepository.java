package com.example.btrack.repository;

import com.example.btrack.models.Update;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateRepository extends JpaRepository<Update, Long> {
}
