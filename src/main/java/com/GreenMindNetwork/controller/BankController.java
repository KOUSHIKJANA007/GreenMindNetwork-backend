package com.GreenMindNetwork.controller;

import com.GreenMindNetwork.payloads.BankDto;
import com.GreenMindNetwork.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
public class BankController {
    @Autowired
    private BankService bankService;

    @PutMapping("/{bankId}")
    public ResponseEntity<BankDto> updateBank(@RequestBody BankDto bankDto, @PathVariable Integer bankId){
        BankDto bankDetails = this.bankService.updateBank(bankDto, bankId);
        System.out.println(bankDto);
        return ResponseEntity.ok(bankDetails);
    }
    @GetMapping("/{ngoId}")
    public ResponseEntity<BankDto> getBankDetails(@PathVariable Integer ngoId){
        BankDto bankByNgo = this.bankService.getBankByNgo(ngoId);
        return ResponseEntity.ok(bankByNgo);
    }
}
