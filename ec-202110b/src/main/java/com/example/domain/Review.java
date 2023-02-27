package com.example.domain;

/**
 * レビュー投稿用ドメイン
 * 1129実装中
 * @author igayuki
 *
 */
public class Review {
	
	private Integer id;
	
	private String name;
	
	private String review;
	
	private Integer userId;
	
	private Integer itemId;
	
	public Review() {
		super();
	}
	
	public Review(Integer id, String name, String review, Integer userId, Integer itemId) {
		super();
		this.id = id;
		this.name = name;
		this.review = review;
		this.userId = userId;
		this.itemId = itemId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", name=" + name + ", review=" + review + ", userId=" + userId + ", itemId="
				+ itemId + "]";
	}

}
