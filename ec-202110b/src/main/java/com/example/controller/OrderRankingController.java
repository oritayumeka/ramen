package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.service.OrderRankingService;

@Controller
@RequestMapping("/orderRanking")
public class OrderRankingController {
	
	@Autowired
	OrderRankingService orderRankingService;
	
	@RequestMapping("/showTotalRanking")
	public String  showTotalRanking(Model model) {
		
		List<Item> topTenItems = orderRankingService.topTenpopularItems();
		
		List<Integer> yearList = new ArrayList<>();
		for(int i = 2010;i <=2021;i++) {
			yearList.add(i);
		}
		List<Integer> monthList = new ArrayList<>();
		for(int j = 1;j <= 12;j++) {
			monthList.add(j);
		}

		model.addAttribute("topTenItems",topTenItems);
		model.addAttribute("yearList",yearList);
		
		model.addAttribute("monthList",monthList);
		
		
		return "/orderRanking/total_order_ranking";
		
	}
	
	@RequestMapping("/showTotalRankingByMonth")
	public String  showMonthRanking(Integer orderYear,Integer orderMonth,Model model) {
		Integer orderYearByInt = orderYear;
		Integer orderMonthByInt = orderMonth;
		
		List<Item> topTenItemsByMonth = orderRankingService.topTenpopularItemsByMonth(orderYearByInt, orderMonthByInt);
		
		List<Integer> yearList = new ArrayList<>();
		for(int i = 2010;i <=2021;i++) {
			yearList.add(i);
		}
		List<Integer> monthList = new ArrayList<>();
		for(int j = 1;j <= 12;j++) {
			monthList.add(j);
		}
		
		model.addAttribute("topTenItemsByMonth",topTenItemsByMonth);
		model.addAttribute("yearList",yearList);
		model.addAttribute("monthList",monthList);
		
		return "/orderRanking/month_order_ranking";
		
	}

}
