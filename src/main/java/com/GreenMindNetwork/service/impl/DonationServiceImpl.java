package com.GreenMindNetwork.service.impl;

import java.util.List;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.GreenMindNetwork.entities.Donation;
import com.GreenMindNetwork.entities.User;
import com.GreenMindNetwork.exception.ResourceNotFoundException;
import com.GreenMindNetwork.payloads.DonationDto;
import com.GreenMindNetwork.payloads.PaymentRequest;
import com.GreenMindNetwork.repositories.DonationRepo;
import com.GreenMindNetwork.repositories.UserRepo;
import com.GreenMindNetwork.service.DonationService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
public class DonationServiceImpl implements DonationService{
	
	@Autowired
	private DonationRepo donationRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<DonationDto> getDonationByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createOrder(PaymentRequest paymentRequest,Integer userId) throws RazorpayException {
        User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		int amount=Integer.parseInt(paymentRequest.getAmount().toString());
		RazorpayClient razorpayClient = new RazorpayClient("rzp_test_k0FnFuJJ5UXZ3l", "ZLR0e1oea5mAcN5i5DW6Bhkr");
		JSONObject ob=new JSONObject();
		ob.put("amount", amount*100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_235425");
		System.out.println("2");
		Order order = razorpayClient.orders.create(ob);
		
		DonationDto donationDto=new DonationDto();
		donationDto.setAmount(order.get("amount")+"");
		donationDto.setOrderId(order.get("id"));
		donationDto.setStatus(order.get("status"));
		donationDto.setCreateDate(order.get("created_at"));
		donationDto.setUser(user);
		Donation paymentData = this.modelMapper.map(donationDto, Donation.class);
		this.donationRepo.save(paymentData);
		return order.toString();
	}

	@Override
	public void updateOrder(PaymentRequest paymentRequest) {
		 Donation findByOrderId = this.donationRepo.findByOrderId(paymentRequest.getOrderId());
		findByOrderId.setStatus(paymentRequest.getStatus());
		findByOrderId.setPaymentId(paymentRequest.getPaymentId());
		this.donationRepo.save(findByOrderId);
	}

}
