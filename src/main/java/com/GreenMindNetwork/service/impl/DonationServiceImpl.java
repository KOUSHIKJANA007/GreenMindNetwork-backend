package com.GreenMindNetwork.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.GreenMindNetwork.entities.Event;
import com.GreenMindNetwork.entities.Ngo;
import com.GreenMindNetwork.payloads.EventDto;
import com.GreenMindNetwork.repositories.EventRepo;
import com.GreenMindNetwork.repositories.NgoRepo;
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
	private NgoRepo  ngoRepo;
	@Autowired
	private EventRepo eventRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<DonationDto> getDonationByUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createOrder(PaymentRequest paymentRequest,Integer userId,Integer ngoId) throws RazorpayException {
        User user = this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "id", userId));
		Ngo ngo = this.ngoRepo.findById(ngoId).orElseThrow(() -> new ResourceNotFoundException("Ngo", "id", ngoId));
		int amount=Integer.parseInt(paymentRequest.getAmount());
		RazorpayClient razorpayClient = new RazorpayClient("rzp_test_k0FnFuJJ5UXZ3l", "ZLR0e1oea5mAcN5i5DW6Bhkr");
		JSONObject ob=new JSONObject();
		ob.put("amount", amount*100);
		ob.put("currency", "INR");
		ob.put("receipt", "txn_235425");
		Order order = razorpayClient.orders.create(ob);

		DonationDto donationDto=new DonationDto();
		donationDto.setAmount(order.get("amount")+"");
		donationDto.setOrderId(order.get("id"));
		donationDto.setStatus(order.get("status"));
		donationDto.setCreateDate(order.get("created_at"));
		donationDto.setUser(user);
		donationDto.setNgo(ngo);
		Donation paymentData = this.modelMapper.map(donationDto, Donation.class);
		this.donationRepo.save(paymentData);
		return order.toString();
	}

	@Override
	public void updateOrder(PaymentRequest paymentRequest,Integer eventId) {
		Event event = this.eventRepo.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));
		Donation findByOrderId = this.donationRepo.findByOrderId(paymentRequest.getOrderId());
		findByOrderId.setStatus(paymentRequest.getStatus());
		int amount = Integer.parseInt(findByOrderId.getAmount());
		double finalAmount=(double)amount/100.0;
		findByOrderId.setPaymentId(paymentRequest.getPaymentId());
		double preAmount=event.getCollectedAmount();
		event.setCollectedAmount(preAmount+finalAmount);
		this.eventRepo.save(event);
		this.donationRepo.save(findByOrderId);
	}

}
