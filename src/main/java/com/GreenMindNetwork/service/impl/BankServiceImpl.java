package com.GreenMindNetwork.service.impl;

import com.GreenMindNetwork.entities.Bank;
import com.GreenMindNetwork.entities.Ngo;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.BankDto;
import com.GreenMindNetwork.repositories.BankRepo;
import com.GreenMindNetwork.repositories.NgoRepo;
import com.GreenMindNetwork.service.BankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankServiceImpl implements BankService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BankRepo bankRepo;
    @Autowired
    private NgoRepo ngoRepo;
    @Override
    public void createBank(Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Ngo", "id", ngoId));
        Bank bank = new Bank();
        bank.setAccHolderName(null);
        bank.setAccountNumber(null);
        bank.setIfsc(null);
        bank.setNgo(ngo);
        this.bankRepo.save(bank);
    }

    @Override
    public BankDto updateBank(BankDto bankDto, Integer bankId) {
        Bank bank = this.bankRepo.findById(bankId).orElseThrow(() -> new ResourceNotFoundException("Bank details", "id", bankId));
        bank.setAccHolderName(bankDto.getAccHolderName());
        bank.setAccountNumber(bankDto.getAccountNumber());
        bank.setIfsc(bankDto.getIfsc());
        Bank savedBankDetails = this.bankRepo.save(bank);
        return this.modelMapper.map(savedBankDetails,BankDto.class);
    }

    @Override
    public BankDto getBankByNgo(Integer ngoId) {
        Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Bank details", "id", ngoId));
        Bank byNgo = this.bankRepo.findByNgo(ngo);
        return this.modelMapper.map(byNgo,BankDto.class);
    }
}
