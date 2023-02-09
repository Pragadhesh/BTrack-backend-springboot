package com.example.btrack.models;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Assist")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Assist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userdetails user;

    @ManyToOne
    @JoinColumn(name = "assistant_id")
    private Userdetails assistant;

    @PrePersist
    @PreUpdate
    public void validateAssist() throws Exception {
        if (user.equals(assistant)) {
            throw new Exception("User and assistant must be different.", null);
        }
    }
}

