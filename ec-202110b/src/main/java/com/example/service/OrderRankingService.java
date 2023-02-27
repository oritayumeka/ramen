package com.example.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Item;
import com.example.repository.ItemRepository;

@Service
public class OrderRankingService {
	
	@Autowired
	ItemRepository itemRepository;
	
	
	/**
	 * 総合ランキング用検索メソッド
	 * @return 総合ランキング10位までの商品リスト
	 */
	public List<Item> topTenpopularItems() {
		
		return itemRepository.topTenpopularItems();
		
	}
	
	/**
	 * 月間ランキング用検索メソッド
	 * @param orderYear1 コントローラーから送られてくるランキング対象年  
	 * @param orderMont2 コントローラーから送られてくるランキング対象月
	 * @return
	 */
	public List<Item> topTenpopularItemsByMonth(Integer orderYear1,Integer orderMonth1) {
		
		Timestamp orderDate1 =null;
		Timestamp orderDate2 = null;
		
		Integer orderYear2 = 0;
		Integer orderMonth2 = 0;
		
		if(orderMonth1 != 12) {
			orderYear2 = orderYear1;
			orderMonth2 = orderMonth1 + 1;
		}else {
			orderYear2 = orderYear1+1;
			orderMonth2 = 1;
		}
		String str1 = orderYear1 +"-" + orderMonth1+"-01 00:00:00";
		String str2 = orderYear2 +"-" + orderMonth2+"-01 00:00:00";
		
		
		 orderDate1 = Timestamp.valueOf(str1);
	     
	     orderDate2 = Timestamp.valueOf(str2);
		
		return itemRepository.topTenpopularItemsByMonth(orderDate1,orderDate2);
		
	}

}
