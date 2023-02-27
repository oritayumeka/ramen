package com.example.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.domain.Item;
import com.example.domain.Review;
import com.example.domain.User;
import com.example.form.ReviewForm;
import com.example.service.ReviewService;

/**
 * レビュー投稿用コントローラー
 * 20211129実装中
 * @author igayuki
 *
 */
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	@Autowired
	HttpSession session;
	
	@Autowired
	ReviewService reviewService;
	
	@Autowired
	ItemController itemController;
	
	@RequestMapping("/insert")
	public String insert(@Validated ReviewForm reviewForm,BindingResult result,
			Integer itemId,RedirectAttributes redirectAttributes) {
		
		if(result.hasErrors()) {
			
			return "forward:/item/showDetail?id=" + itemId;
		}
		
		List<User> userList = (List<User>)session.getAttribute("user");
		if(userList == null) {
			String message = "レビューの投稿はログインしていないと出来ません";
			redirectAttributes.addAttribute("message",message);
			return "redirect:/item/showDetail?id=" + itemId;
		}
		Review review = new Review();
		BeanUtils.copyProperties(reviewForm, review);
		
		review.setUserId(userList.get(0).getId());
		review.setItemId(itemId);
		reviewService.insert(review);
		
		return "redirect:/item/showDetail?id=" + itemId;
		
	}
	
}
