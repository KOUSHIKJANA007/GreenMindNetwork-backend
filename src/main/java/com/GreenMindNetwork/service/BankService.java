package com.GreenMindNetwork.service;

import com.GreenMindNetwork.payloads.BankDto;

public interface BankService {
    void createBank(Integer ngoId);
    BankDto updateBank(BankDto bankDto,Integer bankId);
    BankDto getBankByNgo(Integer ngoId);
}
