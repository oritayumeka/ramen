package com.example.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.User;
import com.example.form.OrderForm;
import com.example.form.OrderItemForm;
import com.example.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@ModelAttribute
	public OrderForm setUpOrderForm() {
		return new OrderForm();
	}
	
	@Autowired
	HttpSession session;
	
	@Autowired
	ServletContext application;
	
	@Autowired
	CartService cartService;
	
	/*--------------------------カートに追加--------------------------*/
	
	@RequestMapping("/insert")
	public String insertToCart(OrderItemForm orderItemForm,Integer itemId) {
		
		//Form→Domainに詰め替え
		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(itemId);
		orderItem.setQuantity(Integer.parseInt(orderItemForm.getQuantity()));
		orderItem.setSize(orderItemForm.getSize().charAt(0));
		orderItem.setItem(cartService.loadForItem(itemId));
		
		//System.out.println(orderItem);
		//BeanUtils.copyProperties(orderItemForm, orderItem);
		
		List<Integer> orderToppingListByInteger = orderItemForm.getOrderToppingList();
		//orderItemにセットするorderToppingリスト（formで受け取ったリストはInteger→orderToppingで詰め替え）
		List<OrderTopping> orderToppingList = new ArrayList<>();
		for(Integer toppingId : orderToppingListByInteger) {
			//toppingId受取の動作確認
			
			OrderTopping orderTopping = new OrderTopping();
			orderTopping.setOrderItemId(orderItem.getId());
			orderTopping.setToppingId(toppingId);
			orderTopping.setTopping(cartService.loadForTopping(toppingId));
			orderToppingList.add(orderTopping);
		}
		
		//orderToppingを詰めたorderToppingListをorderItemにセットする
		orderItem.setOrderToppingList(orderToppingList);
		
		//sessionスコープ内のuserドメイン)(List<User>)を取り出す
		List<User> userList = (List<User>)session.getAttribute("user");
		
		//下記はログインしてないときのカート追加)
		//ログインしているかのチェック
		
		if(userList == null) {
			
			//既にカートに商品リスト入っているかチェック
			if(session.getAttribute("orderItemList") == null) {
				//入ってなかったらリストを作ってそこに商品を詰めてスコープに入れる
				List<OrderItem> orderItemList = new LinkedList<>();
				orderItemList.add(orderItem);
				session.setAttribute("orderItemList", orderItemList);
			}else {
				//入ってたら取り出したリストに商品をつめる
				
				List<OrderItem> orderItemList = (List<OrderItem>)session.getAttribute("orderItemList");
				orderItemList.add(orderItem);
				session.setAttribute("orderItemList", orderItemList);
			}
			return "redirect:/cart/showCart"; 
		}
		
		//以下はログインしている場合
		
		//1件のみあるUserドメインを取る
		User user =userList.get(0);
		
		//userとorderItemをサービスに渡してインサート
		
		cartService.insert(user,orderItem);
		//カートに商品が登録された状態でカートリスト画面に遷移
		return "redirect:/cart/showCart";
	}
	
	/*--------------------------カートから削除--------------------------*/
	
	/**
	 * 削除ボタンが押された際にカートから商品を削除するメソッド
	 * @param orderItem　htmlから渡される削除対象ドメイン
	 */
	@RequestMapping("/delete")
	public String deleteFromCart(Integer orderItemId,String index) {
		
		//sessionスコープ内のuserドメイン)(List<User>)を取り出す
		List<User> userList = (List<User>)session.getAttribute("user");
		//ログインしているかのチェック
		if(userList == null) {
			List<OrderItem> orderItemList =(List<OrderItem>)session.getAttribute("orderItemList");
			orderItemList.remove(Integer.parseInt(index));
			if(orderItemList.size() == 0) {
				session.removeAttribute("orderItemList");
			}
		}
		
		//ログインしている場合
		//サービスに削除対象ドメインを渡して削除
		cartService.delete(orderItemId);
		//カートから対象商品が削除された状態でカートリスト画面に遷移
		return "forward:/cart/showCart";
		
	}	
	/*--------------------------カートの中身表示--------------------------*/
	@RequestMapping("/showCart")
	public String showCart() {
		
		//cartに表示するお勧め商品（人気Top5）をスコープに入れる
		//ユーザー属性データなどで切り替えないため、アプリケーションスコープ
		application.setAttribute("topFivePopularItems",cartService.topFivepopularItems());
		
		//sessionスコープ内のuserドメイン(List<User>)を取り出す
		List<User> userList = (List<User>)session.getAttribute("user");
		//ログインしているかのチェック
		if(userList == null) {
		
			//ログインしていなかった場合
			
			List<OrderItem> orderItemList = (List<OrderItem>)session.getAttribute("orderItemList");
			if(orderItemList == null){
				//そもそもログイン前にカートに追加していない場合
				
				return "/cart/cart_list_noItem";
					}else {
						//ログイン前にカートへの追加を一度でも行った場合(1度追加して削除した場合リストはnullでなくなる)
						if(orderItemList.size() ==0) {
							
							return "/cart/cart_list_noItem";
						}
						
						//カートの中身がある場合
						//htmlに渡す情報を用意
						
						List<Integer> subTotalList = new LinkedList<>();
						int tax = 0;
						int totalPrice = 0;
						
						//以下ログインしている場合と同様に表示する小計を計算
						for(OrderItem orderItem : orderItemList) {
							int subTotal = orderItem.getSubTotal();
							subTotalList.add(subTotal);
						}
						//orderをnewして計算をさせる
						Order order = new Order();
						order.setOrderItemList(orderItemList);
						tax = order.getTax();
						totalPrice = order.getCalcTotalPrice();
						
						session.setAttribute("orderItemList", orderItemList);
						session.setAttribute("subTotalList", subTotalList);
						session.setAttribute("tax", tax);
						session.setAttribute("totalPrice", totalPrice);
						return "/cart/cart_list";
					}
		}
		
		//ログインしていた場合
		
		//1件のみあるUserドメインを取る
		User user =userList.get(0);
		//検索実行
		Order order = cartService.findAllForCart(user.getId());
		//検索結果（カートの中身）がない場合
		if(order == null) {
			return "/cart/cart_list_noItem";
		}
		
		//手に入れたorderからhtmlに渡すOrderItemListと金額を取り出す
		List<OrderItem> orderItemList = order.getOrderItemList();
		
		//商品＋トッピングの小計を計算
		//計算結果をリストに入れる
		List<Integer> subTotalList = new ArrayList<>();
		
		for(OrderItem orderItem : orderItemList) {
			int subTotal = orderItem.getSubTotal();
			subTotalList.add(subTotal);
		}
		
		//消費税計算
		int tax = order.getTax();
		
		//合計金額計算
		int totalPrice = order.getCalcTotalPrice();
		//合計金額をorderドメインに入れる
		order.setTotalPrice(totalPrice);
		
		//sessionスコープに入れてhtmlに渡す
		session.setAttribute("orderItemList", orderItemList);
		session.setAttribute("subTotalList", subTotalList);
		session.setAttribute("tax", tax);
		session.setAttribute("totalPrice", totalPrice);
		return "/cart/cart_list";
	}
	/*--------------------------カートを統合（ログイン前後）--------------------------*/
	@RequestMapping("/combineCart")
	public String combineCart() {
		
		//sessionからログイン前にカート追加して、その後ログインしたUserとカートの中身を取り出す
		List<User> userList = (List<User>)session.getAttribute("user");
		List<OrderItem> orderItemList = (List<OrderItem>)session.getAttribute("orderItemList");
		User user =userList.get(0);
		
		//カートの中身をインサート
		for(OrderItem orderItem : orderItemList) {
			cartService.insert(user, orderItem);
		}
		
		//最新のカート内容を検索
		Order order = cartService.findAllForCart(user.getId());
		
		//手に入れたorderからhtmlに渡すOrderItemListと金額を取り出す
				orderItemList = order.getOrderItemList();
				
				//商品＋トッピングの小計を計算
				//計算結果をリストに入れる
				List<Integer> subTotalList = new ArrayList<>();
				
				for(OrderItem orderItem : orderItemList) {
					int subTotal = orderItem.getSubTotal();
					subTotalList.add(subTotal);
				}
				
				//消費税計算
				int tax = order.getTax();
				
				//合計金額計算
				int totalPrice = order.getCalcTotalPrice();
				//合計金額をorderドメインに入れる
				order.setTotalPrice(totalPrice);
				
				//sessionスコープに入れてhtmlに渡す
				session.setAttribute("orderItemList", orderItemList);
				session.setAttribute("subTotalList", subTotalList);
				session.setAttribute("tax", tax);
				session.setAttribute("totalPrice", totalPrice);
		
				return "order/order_confirm";
	}
}
