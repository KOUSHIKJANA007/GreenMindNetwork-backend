package com.GreenMindNetwork.payloads;

import java.util.Date;

import com.GreenMindNetwork.entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonationDto {

	private long id;
	private String orderId;
	private String amount;
	private String status;
	private String paymentId;
	private User user;
	private Date createDate;
}
