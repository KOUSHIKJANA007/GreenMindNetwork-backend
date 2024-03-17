package com.GreenMindNetwork.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Donation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String orderId;
	private String amount;
	private String status;
	private String paymentId;
	@ManyToOne
	private User user;
	@ManyToOne
	private Ngo ngo;
	private Date createDate;
	
}
