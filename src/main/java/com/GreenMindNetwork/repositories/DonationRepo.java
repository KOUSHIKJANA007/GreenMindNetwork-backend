package com.GreenMindNetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GreenMindNetwork.entities.Donation;
import com.GreenMindNetwork.payloads.DonationDto;

public interface DonationRepo extends JpaRepository<Donation, Long>{

	Donation findByOrderId(String orderId);
}
