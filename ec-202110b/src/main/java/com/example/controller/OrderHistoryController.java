package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.User;
import com.example.service.OrderHistoryService;

@Controller
@RequestMapping("/orderHistory")
public class OrderHistoryController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	OrderHistoryService orderHistoryService;
	
	@RequestMapping("")
	public String showOrderHistory(Model model) {
		
		//sessionからUserが1件だけ入ったリスト
		List<User> userList =(List<User>)session.getAttribute("user");
		
		if(userList == null) {
			//ログインしていなければログイン画面に遷移
			session.setAttribute("fromOrderHistory", "");
			return "forward:/user/toLogin";
		}
		
		List<Order> orderList = orderHistoryService.findOrderHistory(userList.get(0).getId());
		
		
		//注文がこれまでにある場合
		if(orderList != null) {
			
			//もう一度買うようのorderItemList(resultOrderItemList(重複なし))を取り出す
			List<OrderItem> resultOrderItemList = new ArrayList<>();
			Map<Integer, OrderItem> orderItemMap= new HashMap<>();
			
			for(Order order : orderList) {
				List<OrderItem> orderItemList = order.getOrderItemList();
				for(OrderItem orderItem : orderItemList) {
					if(orderItemMap.get(orderItem.getItemId()) == null) {
						resultOrderItemList.add(orderItem);
						orderItemMap.put(orderItem.getItemId(), orderItem);
					}
				}
			}
			model.addAttribute("resultOrderItemList",resultOrderItemList);
			model.addAttribute("orderList",orderList);
			return "orderHistory/order_history";
		}
		
		//これまでにorderがなかったら
		return "orderHistory/order_no_history";
		
		
	}
	
	@RequestMapping("/showDetail")
	public String showOrderHistoryDetail(Integer orderId,Model model) {
		Order order = orderHistoryService.findOrderHistoryByOne(orderId);
		
		List<Integer> subTotalList = new ArrayList<>();
		
		for(OrderItem orderItem : order.getOrderItemList()) {
			int subTotal = orderItem.getSubTotal();
			subTotalList.add(subTotal);
		}
		
		model.addAttribute("order",order);
		model.addAttribute("subTotalList",subTotalList);
		
		return "orderHistory/order_history_detail";
		
	}

}
