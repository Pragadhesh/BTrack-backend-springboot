package com.example.btrack.models;

import java.util.Date;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Update")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Update {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date lastupdated;
}

