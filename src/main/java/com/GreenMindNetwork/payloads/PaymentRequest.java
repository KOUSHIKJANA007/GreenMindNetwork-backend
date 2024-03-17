package com.GreenMindNetwork.payloads;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentRequest {

	private String amount;
	private String paymentId;
	private String orderId;
	private String status;
}
