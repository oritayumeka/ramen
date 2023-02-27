package com.example.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.domain.Review;
import com.example.form.ItemForm;
import com.example.form.OrderItemForm;
import com.example.form.ReviewForm;
import com.example.repository.ReviewRepository;
import com.example.service.ItemService;
import com.example.service.ReviewService;


@Controller
@RequestMapping("/item")
public class ItemController {
	/**入力画面表示
	 * @return
	 */
	@RequestMapping("")
	public String showList(Model model) {
		List<Item> itemList = itemService.findAllOrderByPopular();
		model.addAttribute("itemList",itemList);
		
		return "/item/item_list_noodle";
	}
	
	@Autowired
	private ItemService itemService;
	
	@ModelAttribute
	private ItemForm itemForm() {
		return new ItemForm();
	}
	
	//レビュー用
	@ModelAttribute
	public ReviewForm setUpReviewForm() {
		
		return new ReviewForm();
		
	}
	
	//レビュー用
	@Autowired
	ReviewService reviewService;
	
	
	/**名前の部分検索
	 * @param itemForm
	 * @param model
	 * @return
	 */
	@RequestMapping("/search")
	public String searchItem(ItemForm itemForm,Model model) {
		
		if(itemForm.getSortType().equals("priceHigh")) {
			List<Item> itemList = itemService.oderByPriceHigh(itemForm.getName());
			model.addAttribute("itemList",itemList).addAttribute("number",1);
			if(itemList.size()==0) {
			model.addAttribute("message","該当の商品が見つかりません");
			itemList = itemService.findAllOrderByPriceHigh();
			model.addAttribute("itemList",itemList);
			}
			return "/item/item_list_noodle";
		}else if(itemForm.getSortType().equals("priceLow")) {
			List<Item> itemList = itemService.orderByPriceLow(itemForm.getName());
			model.addAttribute("itemList",itemList).addAttribute("number",2);;
			if(itemList.size()==0) {
			model.addAttribute("message","該当の商品が見つかりません");
			itemList = itemService.findAllOrderByPriceLow();
			model.addAttribute("itemList",itemList);
			}
			return "/item/item_list_noodle";
		}else if(itemForm.getSortType().equals("popular")) {
			List<Item> itemList = itemService.orderByPopular(itemForm.getName());
			model.addAttribute("itemList",itemList).addAttribute("number",3);;
			if(itemList.size()==0) {
			model.addAttribute("message","該当の商品が見つかりません");
			itemList = itemService.findAllOrderByPopular();
			model.addAttribute("itemList",itemList);
			}
			return "/item/item_list_noodle";
		}
		List<Item> itemList = itemService.orderByPopular(itemForm.getName());
		if(itemList.size()==0) {
			model.addAttribute("message","該当の商品が見つかりません");
			itemList = itemService.findAllOrderByPopular();
			model.addAttribute("itemList",itemList);
		
			
		
		}
		model.addAttribute("itemList",itemList);
		return "/item/item_list_noodle";
		
	}
	
	
	@ModelAttribute
	public OrderItemForm setUpOrderItemForm() {
//		return new OrderItemForm();
		OrderItemForm form = new OrderItemForm();
		form.setSize("M");
		return form;
	}
	@RequestMapping("/showDetail")
	public String showDetail(Integer id,Model model) {
		Item item =new Item();
		item = itemService.load(id);
		item.setToppingList(itemService.showAll());
		model.addAttribute("item", item);
		
		//レビュー読み込み
		List<Review> reviewList =  reviewService.showReviewByItemId(id);
		model.addAttribute("reviewList",reviewList);
		
		return "/item/item_detail";
	}
}