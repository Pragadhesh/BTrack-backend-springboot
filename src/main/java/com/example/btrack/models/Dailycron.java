package com.example.btrack.models;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Dailycron")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Dailycron {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_execution_date")
    private LocalDate lastExecutionDate;

    @Column(name = "is_executed_today")
    private Boolean isExecutedToday;

    // Getter and Setter for id
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


}

