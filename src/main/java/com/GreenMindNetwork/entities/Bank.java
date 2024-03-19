package com.GreenMindNetwork.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String accountNumber;
    private String ifsc;
    @Column(name = "account holder name")
    private String accHolderName;
    @OneToOne
    private Ngo ngo;

}
