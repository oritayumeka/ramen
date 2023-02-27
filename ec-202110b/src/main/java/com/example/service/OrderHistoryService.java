package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Order;
import com.example.repository.OrderHistoryRepository;

@Service
public class OrderHistoryService {
	
	@Autowired
	OrderHistoryRepository orderHistoryRepository;
	
	public List<Order> findOrderHistory(Integer userId) {
		
		List<Order> orderList = orderHistoryRepository.findOrderHistory(userId);
		
		return orderList;
	}
	
	public Order findOrderHistoryByOne(Integer orderId) {
		//１件だけのOrderリスト
		List<Order> order= orderHistoryRepository.findOrderHistoryByOne(orderId);
		return order.get(0);
	}
	
}
