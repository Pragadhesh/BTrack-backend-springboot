package com.example.btrack.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Userdetails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String email;
}
