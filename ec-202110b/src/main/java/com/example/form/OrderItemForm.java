package com.example.form;

import java.util.List;

import com.example.domain.Item;
import com.example.domain.OrderTopping;

/**
 * 商品詳細画面で使用する商品注文用フォームクラス
 * @author igayuki
 */
public class OrderItemForm {
	
	
	private String itemId;
	
	private String quantity;
	
	private String size;
	
	//htmlからはInteger型のリストで受け取る
	//各ナンバーはToppingのID
	private  List<Integer> orderToppingList;

	public OrderItemForm() {
		super();
	}

	public OrderItemForm(String itemId, String quantity, String size, List<Integer> orderToppingList) {
		super();
		this.itemId = itemId;
		this.quantity = quantity;
		this.size = size;
		this.orderToppingList = orderToppingList;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public List<Integer> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<Integer> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}

	@Override
	public String toString() {
		return "OrderItemForm [itemId=" + itemId + ", quantity=" + quantity + ", size=" + size + ", orderToppingList="
				+ orderToppingList + "]";
	}
	
	
	

	
}
