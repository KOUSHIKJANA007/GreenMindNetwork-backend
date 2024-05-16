package com.GreenMindNetwork.payloads;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventProgressDto{
    private int id;
    private int progress;
    private String imageName;
    private String caption;
    private long totalCost;
    private long remainAmount;
    @Size(min = 100,max = 1000,message = "Size must be minimum 100 character and maximum 1000")
    private String description;
    private EventDto event;
}
