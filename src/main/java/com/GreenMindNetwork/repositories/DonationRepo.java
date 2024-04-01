package com.GreenMindNetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.GreenMindNetwork.entities.Donation;
import com.GreenMindNetwork.payloads.DonationDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DonationRepo extends JpaRepository<Donation, Long>{

	Donation findByOrderId(String orderId);
	@Query("select SUM(p.amount) from Donation p where p.user.id like :userId and p.status='paid'")
	Integer getTotalDonate(@Param("userId") Integer userId);
	@Query("select SUM(p.amount) from Donation p where p.ngo.id like :ngoId and p.status='paid'")
	Integer getTotalNgoAmount(@Param("ngoId") Integer ngoId);
}
