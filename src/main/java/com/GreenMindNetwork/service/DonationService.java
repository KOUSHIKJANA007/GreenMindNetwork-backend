package com.GreenMindNetwork.service;

import java.util.List;

import com.GreenMindNetwork.payloads.DonationDto;
import com.GreenMindNetwork.payloads.PaymentRequest;
import com.razorpay.RazorpayException;

public interface DonationService {

	List<DonationDto> getDonationByUser();
	String createOrder(PaymentRequest paymentRequest,Integer userId,Integer ngoId) throws RazorpayException ;
	void updateOrder(PaymentRequest paymentRequest,Integer eventId);
	Integer getTotalDonationAmount(Integer userId);
}
