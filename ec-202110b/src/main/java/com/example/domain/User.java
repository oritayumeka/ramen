package com.example.domain;

/**
 * @author igayuki
 * ユーザー用ドメイン
 */
public class User {
	
	private Integer id;
	
	private String name;
	
	private String email;
	
	private String password;
	
	private String zipcode;
	
	private String address;
	
	private String telephone;
	
	private String  passwordConfirm;

	public User() {
		super();
	}

	public User(Integer id, String name, String email, String password, String zipcode, String address,
			String telephone, String passwordConfirm) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.zipcode = zipcode;
		this.address = address;
		this.telephone = telephone;
		this.passwordConfirm = passwordConfirm;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", zipcode="
				+ zipcode + ", address=" + address + ", telephone=" + telephone + ", passwordConfirm="
				+ passwordConfirm + "]";
	}

	
	
	
	

}
