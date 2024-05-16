package com.GreenMindNetwork.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventProgress{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int progress;
    private String imageName;
    private String caption;
    private long totalCost;
    private long remainAmount;
    @Column(length = 1000)
    private String description;
    @ManyToOne
    private Event event;
}
