package com.example.btrack.models;
import javax.persistence.*;
import lombok.*;

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
    private User user;

    private String name;

    private String type;

    private String category;

    private int health;

    private int damage;

    private String usage;

    private int days;

    public void decreaseHealth() {
        if (usage.equals("daily")) {
            health -= damage;
        } else if (usage.equals("weekly")) {
            health -= (damage * days)/7;
        } else if (usage.equals("monthly")) {
            health -= (damage * days)/30;
        } else if (usage.equals("nousage")) {
            health = health;
        }

        if (health < 0) {
            health = 0;
        }
    }

}

