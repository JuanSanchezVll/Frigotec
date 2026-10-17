package com.frigo.frigotec.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Fabricante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
}
