package com.example.domain;

import java.util.List;

/**
 * @author igayuki
 * 注文商品用ドメイン
 */
public class OrderItem {
	
	private Integer id;
	
	private Integer itemId;
	
	private Integer orderId;
	
	private Integer quantity;
	
	private Character size;
	
	private Item item;
	
	private List<OrderTopping> orderToppingList;
	
	//商品＋そのトッピングの小計を計算するメソッド
	public int getSubTotal() {
		int itemPrice = 0;
		int toppingPrice = 0;
		
		if(size == 'M'){
			itemPrice = item.getPriceM();
		}else if(size == 'L') {
			itemPrice = item.getPriceL();
		}
		
		for(OrderTopping orderTopping : orderToppingList){
			
			Topping topping = orderTopping.getTopping();
			if(size == 'M') {
				toppingPrice += topping.getPriceM();
			}else if(size == 'L') {
				toppingPrice += topping.getPriceL();
			}
			
		}
		
		int oneSubTotal = itemPrice + toppingPrice;
		return oneSubTotal * quantity;
		
	}

	public OrderItem() {
		super();
	}

	public OrderItem(Integer id, Integer itemId, Integer orderId, Integer quantity, Character size, Item item,
			List<OrderTopping> orderToppingList) {
		super();
		this.id = id;
		this.itemId = itemId;
		this.orderId = orderId;
		this.quantity = quantity;
		this.size = size;
		this.item = item;
		this.orderToppingList = orderToppingList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getSize() {
		return size;
	}

	public void setSize(Character size) {
		this.size = size;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + "]";
	}
	
}
