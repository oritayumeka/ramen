package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.domain.Review;
import com.example.domain.Topping;
import com.example.repository.ItemRepository;
import com.example.repository.ReviewRepository;

@Service
@Transactional
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	//レビュー表示用レポジトリ
	@Autowired
	ReviewRepository reviewRepository;
	
	/**標品一覧表示
	 * @return
	 */
	public List<Item> findAll(){
		return itemRepository.findAll();
	}
	public List<Item> findAllOrderByPriceLow(){
		return itemRepository.findAllOrderByPriceLow();
	}
	public List<Item> findAllOrderByPriceHigh(){
		return itemRepository.findAllOrderByPriceHigh();
	}
	public List<Item> findAllOrderByPopular(){
		return itemRepository.findAllOrderByPopular();
	}
	/**nameを使って商品を検索
	 * @param name
	 * @return
	 */
	
	public Item load(Integer id) {
		return itemRepository.load(id);
	}
	
	public List<Topping> showAll(){
		return itemRepository.showAll();
	}
	public List<Item> oderByPriceHigh(String name){
		return itemRepository.priceHigh(name);
	}
	public List<Item> orderByPriceLow(String name){
		return itemRepository.priceLow(name);
	}
	public List<Item> orderByPopular(String name){
		return itemRepository.orderBypopular(name);
	}
	
	/**
	 *  レビュー表示用検索メソッド
	 * @param itemId レビューの対象ItemId
	 * @return 対象レビューリスト
	 */
	public List<Review> showReviewByItemId(Integer itemId){
		
		return reviewRepository.showReviewByItemId(itemId);
		
	}
}
