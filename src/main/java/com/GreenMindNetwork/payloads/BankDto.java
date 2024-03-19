package com.GreenMindNetwork.payloads;

import lombok.Data;

@Data
public class BankDto {
    private int id;
    private String accountNumber;
    private String ifsc;
    private String accHolderName;
    private BankDto bank;
}
