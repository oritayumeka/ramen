package com.example.form;

import com.example.domain.Topping;

/**
 * 商品詳細画面で使用するトッピング注文用のフォームクラス
 * @author igayuki
 *
 */
public class OrderToppingForm {
	
	private Integer id;
	
	private Integer toppingId;
	
	private Integer orderItemId;
	
	private Topping topping;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getToppingId() {
		return toppingId;
	}

	public void setToppingId(Integer toppingId) {
		this.toppingId = toppingId;
	}

	public Integer getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Integer orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Topping getTopping() {
		return topping;
	}

	public void setTopping(Topping topping) {
		this.topping = topping;
	}

	@Override
	public String toString() {
		return "OrderToppingForm [id=" + id + ", toppingId=" + toppingId + ", orderItemId=" + orderItemId + ", topping="
				+ topping + "]";
	}
	
	

}
