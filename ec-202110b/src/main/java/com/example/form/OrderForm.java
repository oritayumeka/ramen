package com.example.form;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class OrderForm {
	
	@NotBlank(message="名前を入力して下さい")
	private String destinationName;
	
	@NotBlank(message="メールアドレスを入力して下さい")
	@Email(message="メールアドレスの形式が不正です")
	private String destinationEmail;
	
	@NotBlank(message="郵便番号を入力してください")
	@Pattern(regexp="^[0-9]{3}-[0-9]{4}$|^$", message="郵便番号はXXX-XXXXの形式で入力してください")
	private String zipcode;
	
	@NotBlank(message="住所を入力して下さい")
	private String address;
	
	@NotBlank(message="電話番号を入力してください")
	@Pattern(regexp="^[0-9]{1,4}-[0-9]{1,4}-[0-9]{1,4}$|^$", message="電話番号はXXXX-XXXX-XXXXの形式で入力してください")
	private String destinationTel;
	
	@NotEmpty(message="配達日を入力して下さい")
	private String deliveryDate;
	
	@NotEmpty(message="配達時間を入力して下さい")
	private String deliveryTime;
	
	@NotEmpty(message="支払方法を選択してください")
	private String paymentMethod;
	
	private String token; 
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getDestinationEmail() {
		return destinationEmail;
	}

	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDestinationTel() {
		return destinationTel;
	}

	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}
	
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public String getDeliveryTime() {
		return deliveryTime;
	}
	
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	
	

}
