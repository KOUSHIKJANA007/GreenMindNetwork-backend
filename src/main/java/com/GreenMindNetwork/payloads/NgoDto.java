package com.GreenMindNetwork.payloads;

import com.GreenMindNetwork.entities.Event;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NgoDto {
    private int id;
    @NotNull
    @Size(min=5,max=200,message = "Name should be minimum 5 letter and maximum 200 letter")
    private String name;
    private String nameOfHead;
    @Email
    private String email;
    @NotNull
    private String mobile;
    @NotNull
    @Size(min=5,max=300,message = "Address should be minimum 5 letter and maximum 300 letter")
    private String address;
    @NotNull
    @Size(min=5,max=300,message = "Slogan should be minimum 5 letter and maximum 300 letter")
    private String slogan;
    @NotNull
    @Size(min=500,max=10000,message = "Description should be minimum 100 letter and maximum 2000 letter")
    private String description;
    @NotNull
    private Date establishedDate;
    private String logo;
    private String identityOfHead;
    private String imageOfTax;
    private String registerImage;
    private UserDto user;
}
