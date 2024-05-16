package com.GreenMindNetwork.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Column(length = 10000)
    private String description;
    private Date createDate;
    private double targetAmount;
    private double collectedAmount;
    private String image;
    @ManyToOne
    private Ngo ngo;
    @OneToMany(mappedBy = "event",cascade = CascadeType.ALL)
    List<EventProgress> eventProgresses=new ArrayList<>();
}
