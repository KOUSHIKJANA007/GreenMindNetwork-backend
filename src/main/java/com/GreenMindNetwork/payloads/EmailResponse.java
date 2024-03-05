package com.GreenMindNetwork.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailResponse {
    private String email;
    private String otp;
}
