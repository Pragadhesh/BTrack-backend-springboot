package com.example.btrack.repository;



import com.example.btrack.models.Userdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Userdetails, Long> {
    Optional<Userdetails> findByUsername(String username);

    Optional<Userdetails> findByUsernameIgnoreCaseLike(String name);

    @Query("SELECT u FROM Userdetails u WHERE LOWER(u.username) LIKE LOWER(concat('%', :username, '%')) AND u.id <> :id")
    List<Userdetails> findByUsernameIgnoreCaseLikeAndIdNot(@Param("username") String username, @Param("id") Long id);


}

