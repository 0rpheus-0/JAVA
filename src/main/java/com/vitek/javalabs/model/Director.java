package com.vitek.javalabs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Director {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
