package com.GreenMindNetwork.repositories;

import com.GreenMindNetwork.entities.Bank;
import com.GreenMindNetwork.entities.Ngo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepo extends JpaRepository<Bank,Integer> {
    Bank findByNgo(Ngo ngo);
}
