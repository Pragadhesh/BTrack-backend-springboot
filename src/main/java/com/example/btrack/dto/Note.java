package com.example.btrack.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Note {
    private long id;
    private String title;
    private String description;
}

