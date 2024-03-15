package com.GreenMindNetwork.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private Date createDate;
    private double targetAmount;
    private double collectedAmount;
    private String image;
    @ManyToOne
    private Ngo ngo;
}
