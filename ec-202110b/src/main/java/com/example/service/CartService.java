package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.User;
import com.example.repository.ItemRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

/**
 * カートの処理を行うサービス
 * @author igayuki
 *
 */
@Service
public class CartService {
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	@Autowired
	OrderToppingRepository orderToppingRepository;
	
	@Autowired
	ItemRepository itemRepository;
	
	/*--------------------------カートに追加--------------------------*/
	
	/**
	 * カートに商品追加時のメソッド
	 * OrderItemではOrderの、OrderToppingではOrderItemのIDが必要になるのでそれぞれ検索している
	 * @param カートに商品を追加するユーザーの情報（ドメイン）
	 * @return　商品を追加する注文（カート）に相当するOrderドメイン
	 */
	public void insert(User user,OrderItem orderItem) {
		
		//OrderのInsertを行う
		Integer userId = user.getId();
		//既に注文前のOrder（カート）データがあるか検索
		Order order = orderRepository.searchOrderForCart(userId);
		//nullだったらInsertして注文前のOrder（カート）データを用意
		if(order == null) {
			orderRepository.insert(userId);
			//新規に作成したOrderを取り出す
			order = orderRepository.searchOrderForCart(userId);
		}
		
		//OrderItemのInsertを行う
		//Orderオブジェクトが持っているOrderのIdをOrderItemにセットする
		//DBにインサートする情報をid(自動採番)以外持っているOrderItemをインサート
		Integer orderId = order.getId();
		orderItem.setOrderId(orderId);
		orderItemRepository.insert(orderItem);
		
		//登録したOrderItemオブジェクトを検索する
		//OrderToppingに入れるorderItemのIDを入手する
		Integer orderItemId = orderItemRepository.searchOrderItemForCart(orderId).getId();
		//orderToppingにorderItemIdをセットしつつInsert
		
		for(OrderTopping orderTopping: orderItem.getOrderToppingList()) {
			orderTopping.setOrderItemId(orderItemId);
			orderToppingRepository.insert(orderTopping);
		}
		
		
	}
	
	/*--------------------------カートから削除--------------------------*/
	
	/**
	 * カートから商品を削除した際のメソッド
	 * @param id　カートから削除された商品ID
	 */
	public void delete(Integer orderItemId) {
		
		//orderItemからdeleteする対象商品のID取り出す
		
		//商品とトッピングを削除
		orderItemRepository.delete(orderItemId);
		orderToppingRepository.delete(orderItemId);
		
	}
	
	/*--------------------------カートの中身表示--------------------------*/
	
	/**
	 * @param userId
	 */
	public Order findAllForCart(Integer userId){
		
		Order order = orderRepository.findAllForCart(userId);
		
		return order;
		
	}
	
	/*--------------------------商品とトッピングの個別検索（ログイン前使用）--------------------------*/
	/**
	 * @param ItemId
	 */
	public Item loadForItem(Integer id){
		
		return itemRepository.load(id);
		
	}
	/**
	 * @param ItemId
	 */
	public Topping loadForTopping(Integer id){
		
		return itemRepository.loadForTopping(id);
		
	}
	
	/*--------------------------人気Top5商品(カート内で使用）--------------------------*/
	
	public List<Item> topFivepopularItems() {
		
		return itemRepository.topFivepopularItems();
		
	}
}
