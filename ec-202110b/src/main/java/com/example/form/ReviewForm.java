package com.example.form;

import javax.validation.constraints.NotBlank;

public class ReviewForm {
	
	private String id;
	@NotBlank(message = "名前を入力してください")
	private String name;
	@NotBlank(message = "レビュー内容を入力してください")
	private String review;
	
	private String userId;
	
	private String itemId;
	
	public ReviewForm() {
		super();
	}
	
	public ReviewForm(String id, String name, String review, String userId, String itemId) {
		super();
		this.id = id;
		this.name = name;
		this.review = review;
		this.userId = userId;
		this.itemId = itemId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "ReviewForm [id=" + id + ", name=" + name + ", review=" + review + ", userId=" + userId + ", itemId="
				+ itemId + "]";
	}
	
}
