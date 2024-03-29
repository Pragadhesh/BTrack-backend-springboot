package com.example.btrack.models;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Notes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userdetails user;

    private String title;

    @Column(columnDefinition = "text")
    private String description;

    public Notes(String title, String description, Userdetails actualUser) {
        this.title = title;
        this.description = description;
        this.user = actualUser;
    }
}
