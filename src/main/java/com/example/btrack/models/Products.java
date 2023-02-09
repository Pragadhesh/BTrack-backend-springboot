package com.example.btrack.models;
import javax.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Table(name = "Products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Userdetails user;

    private String name;

    private String description;

    private String image_url;

    private String module;

    private String category;

    private int health;

    private int damage;

    private String usage;

    private int days;

    public Products(Userdetails user, String name, String description, String image_url, String module, String category, int health, int damage, String usage, int days) {
        this.user = user;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.module = module;
        this.category = category;
        this.health = health;
        this.damage = damage;
        this.usage = usage;
        this.days = days;
    }

    public void decreaseHealth() {
        System.out.println("decreasing health");
        double decreasedHealth = 0;
        if (usage.equals("daily")) {
            decreasedHealth = health - damage;
        } else if (usage.equals("weekly")) {
            decreasedHealth = health - (damage * days)/7;
        } else if (usage.equals("monthly")) {
            decreasedHealth = health - (damage * days)/30;
        } else if (usage.equals("nousage")) {
            decreasedHealth = health;
        }
        health = (int) Math.round(decreasedHealth);
        if (health < 0) {
            health = 0;
        }
    }
}

