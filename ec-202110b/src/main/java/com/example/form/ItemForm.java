package com.example.form;

public class ItemForm {

	private String name;
	private String id;
	private String sortType;
	
	public ItemForm() {};
	
	public ItemForm(String name) {
		super();
		this.name = name;
		
	}

	public ItemForm(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}

	public ItemForm(String name, String id, String sortType) {
		super();
		this.name = name;
		this.id = id;
		this.sortType = sortType;
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

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	@Override
	public String toString() {
		return "ItemForm [name=" + name + ", id=" + id + ", sortType=" + sortType + "]";
	}

	
}
