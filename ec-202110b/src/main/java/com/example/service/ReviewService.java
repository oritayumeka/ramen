package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Item;
import com.example.domain.Review;
import com.example.domain.Topping;
import com.example.repository.ItemRepository;
import com.example.repository.ReviewRepository;

/**
 * レビュー投稿用
 * 1129実装中
 * @author igayuki
 *
 */
@Service
public class ReviewService {
	
	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	/**
	 * @param review
	 */
	public void insert(Review review) {
		
		reviewRepository.insert(review);
		
	}
	
	/**
	 * @param itemId
	 * @return
	 */
	public List<Review> showReviewByItemId(Integer itemId) {
		
		return reviewRepository.showReviewByItemId(itemId);
		
	}
	
	public Item findItemForReview(Integer itemId) {
		
		return itemRepository.load(itemId);
		
	}
	
	public List<Topping> findToppingForReview() {
		
		return itemRepository.showAll();
		
	}

}
