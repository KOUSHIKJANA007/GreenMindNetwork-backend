package com.GreenMindNetwork.payloads;

import com.GreenMindNetwork.entities.Ngo;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
public class EventDto {
    private int id;
    private String title;
    private String description;
    private String image;
    private Date createDate;
    private double targetAmount;
    private double collectedAmount;
    private NgoDto ngo;
}
