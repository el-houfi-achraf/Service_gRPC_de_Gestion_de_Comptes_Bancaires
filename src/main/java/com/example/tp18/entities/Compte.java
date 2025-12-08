package com.example.tp18.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compte {
    @Id
    private String id;
    private float solde;
    private String dateCreation;
    private String type;
}

