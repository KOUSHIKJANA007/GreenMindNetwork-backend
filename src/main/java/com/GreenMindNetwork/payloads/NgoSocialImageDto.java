package com.GreenMindNetwork.payloads;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class NgoSocialImageDto {
    private int id;
    private String image;
    private Date createDate;
    private String caption;
    private NgoDto ngo;
}
