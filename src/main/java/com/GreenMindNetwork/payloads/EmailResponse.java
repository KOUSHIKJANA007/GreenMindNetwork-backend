package com.GreenMindNetwork.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailResponse {
    private String email;
    private String otp;
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "password must contain uppercase letter,symbol and number")
    @NotBlank(message = "Please enter password")
    @Size(min = 4,max=10)
    private String newPassword;
}
