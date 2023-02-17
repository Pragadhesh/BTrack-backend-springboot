package com.example.btrack.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private long id;
    private String module;
    private String category;
    private String name;
    private String description;
    private String image_url;
    private int damage;
    private String usage;
    private int days;
    private int health;
}
