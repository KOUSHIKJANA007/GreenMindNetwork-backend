package com.GreenMindNetwork.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class NgoSocialImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String image;
    private Date createDate;
    private String caption;
    @ManyToOne
    private Ngo ngo;
}
