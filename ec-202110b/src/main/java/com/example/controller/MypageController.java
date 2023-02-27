package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import com.example.domain.User;
import com.example.form.UpdateUserForm;
import com.example.service.MypageService;
import com.example.service.UserService;

@Controller
@RequestMapping("/mypage")
public class MypageController {
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private MypageService mypageService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@ModelAttribute
	public UpdateUserForm setupUpdateUserForm() {
		//確認画面から修正ボタンを押したときに入力情報が反映できるように
		UpdateUserForm form = new UpdateUserForm();
		if (session.getAttribute("user") != null) {
			//sessionの"user"をリストに統一
			List<User> userList = (List<User>)session.getAttribute("user");
			User user = userList.get(0);
			form.setName(user.getName());
			form.setEmail(user.getEmail());
			form.setZipcode(user.getZipcode());
			form.setAddress(user.getAddress());
			form.setTelephone(user.getTelephone());
		}
		return form;
	}
	
	@RequestMapping("/toMypage")
	public String toMypage() {
		
		List<User> userList =(List<User>)session.getAttribute("user");
		
		if(userList == null) {
			//ログインしていなければログイン画面に遷移
			return "forward:/user/toLogin";
		}
		
		return "/mypage/mypage";
	}
	
	@RequestMapping("/toUpdateUser")
	public String toUpdateUser(){
		
		List<User> userList =(List<User>)session.getAttribute("user");
		
		if(userList == null) {
			//ログインしていなければログイン画面に遷移
			return "forward:/user/toLogin";
		}
		return "/mypage/updateUser";
	}
	
	@RequestMapping("/toUpdateUserFinished")
	public String toUpdateUserFinished(){
		List<User> userList =(List<User>)session.getAttribute("user");
		
		if(userList == null) {
			//ログインしていなければログイン画面に遷移
			return "forward:/user/toLogin";
		}		
		return "/mypage/updateUserFinished";
	}
	
	@RequestMapping("/toDeleteUserConfirm")
	public String toDelete(){
		List<User> userList =(List<User>)session.getAttribute("user");
		
		if(userList == null) {
			//ログインしていなければログイン画面に遷移
			return "forward:/user/toLogin";
		}
		
		return "/mypage/deleteUserConfirm";
	}
	
	
	
	@RequestMapping("/updateUserConfirm")
	public String updateUser(@Validated UpdateUserForm form, BindingResult result, Model model){
		
		if(result.hasErrors()) {
			return toUpdateUser();
		}
		
		List<User> list = (List<User>) session.getAttribute("user");
		User user = list.get(0);
		
		//formのメールアドレスを持つuserドメインが、データベースにあったらエラーを出す
		if(!user.getEmail().equals(form.getEmail())) {
			if(userService.searchByEmail(form.getEmail()).size() == 1) {
				model.addAttribute("emailError", "すでに登録されているメールアドレスです");
				return toUpdateUser();
			}
		}
		
		BeanUtils.copyProperties(form, user);
		List<User> userList = new ArrayList<>();
		userList.add(user);
		session.setAttribute("user", userList);
		
		return "/mypage/updateUserConfirm";
		
	}
	
	
	@RequestMapping("/updateUser")
	public String updateUser(String password, Model model) {
		//session内の"user"キーはリストに統一
		//1件だけのUserを取り出す
		List<User> userList = (List<User>)session.getAttribute("user");
		User user = userList.get(0);
		
		if (!passwordEncoder.matches(password, user.getPassword())) {
			model.addAttribute("passError", "パスワードが一致しません");
			
			return "/mypage/updateUserConfirm";
			
		}else {
		
		mypageService.update(user);
		return "redirect:/mypage/toUpdateUserFinished";
		}
	}

	@RequestMapping("/deleteUserConfirm")
	public String deleteUserConfirm(String confirm) {
		if("yes".equals(confirm)) {
			return "mypage/deleteUser";
		}else {
			return "mypage/notDeleteUser";
		}
	}
	
	
	
	@RequestMapping("/deleteUser")
	public String deleteUser(String inputPassword, Model model){
		
		List<User> list = (List<User>) session.getAttribute("user");
		Integer id = list.get(0).getId();
		String password = list.get(0).getPassword();
		
		if(passwordEncoder.matches(inputPassword, password)) {
			mypageService.delete(id);
			session.invalidate();
			return "/mypage/deleteUserFinished";
		}else {
			model.addAttribute("passError","パスワードが違います");
			return toDelete();
		}
	}
	
	
}
