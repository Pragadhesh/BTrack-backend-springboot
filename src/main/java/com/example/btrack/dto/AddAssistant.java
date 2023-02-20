package com.example.btrack.dto;

import com.example.btrack.models.Userdetails;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddAssistant {
    Userdetails user;
    String status;
}
