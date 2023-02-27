package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class UpdateUserForm {
		private String id;
		
		@NotBlank(message="名前を入力してください")
		private String name;
		
		@NotBlank(message="メールアドレスを入力してください")
//		@Email(message="メールアドレスの形式で入力してください")
		private String email;
		
		@NotBlank(message="郵便番号を入力してください")
		@Pattern(regexp="^[0-9]{3}-[0-9]{4}$|^$", message="郵便番号はXXX-XXXXの形式で入力してください")
		private String zipcode;
		
		@NotBlank(message="住所を入力してください")
		private String address;
		
		@NotBlank(message="電話番号を入力してください")
		@Pattern(regexp="^[0-9]{1,4}-[0-9]{1,4}-[0-9]{1,4}$|^$", message="電話番号はXXXX-XXXX-XXXXの形式で入力してください")
		private String telephone;
		


		public int getIntId() {
			return Integer.parseInt(id);
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

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
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


		

		
}

	

