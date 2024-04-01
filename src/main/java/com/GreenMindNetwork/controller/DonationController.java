package com.GreenMindNetwork.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.GreenMindNetwork.payloads.ApiResponse;
import com.GreenMindNetwork.payloads.PaymentRequest;
import com.GreenMindNetwork.service.DonationService;
import com.razorpay.RazorpayException;


@RestController
@RequestMapping("/api")
public class DonationController {

	@Autowired
	private DonationService donationService;
	
	@PostMapping("/donation/user/{userId}/ngo/{ngoId}")
	public ResponseEntity<String> createOrder(@RequestBody PaymentRequest paymentRequest,@PathVariable Integer userId,@PathVariable Integer ngoId) throws RazorpayException{
		String createOrder = this.donationService.createOrder(paymentRequest,userId,ngoId);
		return ResponseEntity.ok(createOrder);
	}
	@PutMapping("/donation/{eventId}")
	public ResponseEntity<ApiResponse> updateOrder(@RequestBody PaymentRequest paymentRequest,@PathVariable Integer eventId) throws RazorpayException{
		this.donationService.updateOrder(paymentRequest,eventId);
		ApiResponse apiResponse=new ApiResponse("payment done",true);
		return ResponseEntity.ok(apiResponse);
	}
	@GetMapping("/donation/user/{userId}")
	public ResponseEntity<Integer> getTotalDonationAmount(@PathVariable Integer userId){
		Integer totalDonationAmount = this.donationService.getTotalDonationAmount(userId);
		return ResponseEntity.ok(totalDonationAmount);
	}
	@GetMapping("/donation/ngo/{ngoId}")
	public ResponseEntity<Integer> getTotalDonationAmountByNgo(@PathVariable Integer ngoId){
		Integer totalDonationAmountByNgo = this.donationService.getTotalDonationAmountByNgo(ngoId);
		return ResponseEntity.ok(totalDonationAmountByNgo);
	}
}
