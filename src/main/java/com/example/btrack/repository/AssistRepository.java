package com.example.btrack.repository;

import com.example.btrack.models.Assist;
import com.example.btrack.models.Userdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AssistRepository extends JpaRepository<Assist, Long> {

    @Query("SELECT a FROM Assist a JOIN a.user u JOIN a.assistant ass WHERE u.username = :username AND ass.username = :username1")
    Optional<Assist> findByUser_UsernameAndAssistant_Username(@Param("username") String username, @Param("username1") String username1);


    @Query("SELECT a FROM Assist a JOIN a.user u JOIN a.assistant ass WHERE u.username = :username AND ass.username LIKE %:assistusername% AND a.status != 'accepted'")
    List<Assist> findByUser_UsernameAndAssistant_UsernameLike(@Param("username") String username, @Param("assistusername") String assistusername);


    //second user side
    @Query("SELECT a FROM Assist a JOIN a.user u JOIN a.assistant ass WHERE u.username = :username1 AND ass.username = :username")
    Optional<Assist> findByAssistantAndUser(@Param("username") String username, @Param("username1") String username1);


    List<Assist> findByAssistantAndStatusContaining(Userdetails actualUser, String pending);

    List<Assist> findByUserAndStatusContaining(Userdetails actualUser, String accepted);
}

