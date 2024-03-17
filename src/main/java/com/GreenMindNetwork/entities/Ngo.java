package com.GreenMindNetwork.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ngo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String nameOfHead;
    @Column(unique = true)
    private String email;
    private String mobile;
    private String address;
    private String slogan;
    @Column(length = 10000)
    private String description;
    private Date establishedDate;
    private String logo;
    private String identityOfHead;
    private String imageOfTax;
    private String registerImage;
    @OneToOne
    private User user;

    @OneToMany(mappedBy = "ngo",cascade = CascadeType.ALL)
    private List<Event> event=new ArrayList<>();
    @OneToMany(mappedBy = "ngo",cascade = CascadeType.ALL)
    private List<NgoSocialImage> ngoSocialImages=new ArrayList<>();
    @OneToMany(mappedBy = "ngo",cascade = CascadeType.ALL)
    private List<Donation> donations=new ArrayList<>();
}
