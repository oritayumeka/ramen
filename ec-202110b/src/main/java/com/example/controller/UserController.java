package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.StringUtils;

import com.example.domain.User;
import com.example.form.LoginForm;
import com.example.form.UserForm;
import com.example.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private UserService userService;
	
	@ModelAttribute
	public UserForm setUpUserForm() {
		//return new UserForm();
		//確認画面から修正ボタンを押したときに入力情報が反映できるように
		UserForm form = new UserForm();
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
	
	@ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}
	
	
	
	/**
	 * @return ログイン画面に遷移
	 */
	@RequestMapping("/toLogin")
	public String toLogin() {
		return "/user/login";
	}
	
	
	/**
	 * @return 登録画面に遷移
	 */
	@RequestMapping("/toRegister")
	public String toRegister() {
		return "/user/register_user";
	}
	
	/**
	 * @return 登録画面に遷移
	 */
	@RequestMapping("/toFinish")
	public String tofinish() {
		return "/user/register_finish";
	}
	
	
	
	
	/**
	 * @param form
	 * @return 登録画面から登録確認画面に遷移
	 */
	@RequestMapping("/registerConfirm")
	 public String registerConfirm(@Validated UserForm form, BindingResult result, Model model) {
		if(result.hasErrors()) {
			if(userService.searchByEmail(form.getEmail()).size() == 1) {
				result.rejectValue("email", "", "そのメールアドレスはすでに使われています");
				return toRegister();
			}
			
			if(!form.getPassword().equals(form.getPasswordConfirm()) && (form.getPassword() != "" && form.getPasswordConfirm() != "")){
				result.rejectValue("password", "", "パスワードが一致していません");
				result.rejectValue("passwordConfirm", "", "");
				return toRegister();
			}
			
			return toRegister();
		}
		User user = new User();
		BeanUtils.copyProperties(form, user);
		//session内の"user"キーオブジェクトをリストで統一するため１件のみのリスト作成
		List<User> userList = new ArrayList<>();
		userList.add(user);
		
		session.setAttribute("user", userList);

		//formのメールアドレスを持つuserドメインが、データベースにあったらエラーを出す
		if(userService.searchByEmail(form.getEmail()).size() == 1) {
			result.rejectValue("email", "", "そのメールアドレスはすでに使われています");
			return toRegister();
		}
		
		
		if(!form.getPassword().equals(form.getPasswordConfirm()) && (form.getPassword() != "" && form.getPasswordConfirm() != "")){
			result.rejectValue("password", "", "パスワードが一致していません");
			result.rejectValue("passwordConfirm", "", "");
			return toRegister();
		}
		
		
		
		//パスワードを確認画面で●表示させる
		String password = user.getPassword();
		int length = password.length();
		model.addAttribute("passwordMaru", StringUtils.repeat("●", length));
		return "/user/register_confirm";
	}
	
	
	/**
	 * @param form
	 * @return 登録確認画面から完了画面に遷移
	 */
	@RequestMapping("/register")
	public String register(UserForm form, Model model) {
		//session内の"user"キーはリストに統一
		//1件だけのUserを取り出す
		List<User> userList = (List<User>)session.getAttribute("user");
		User user = userList.get(0);
		userService.insert(user);
		session.removeAttribute("user");
		
		return "redirect:/user/toFinish";
	}

	
	/**
	 * @param form
	 * @return 商品リストに遷移
	 */
	@RequestMapping("/login")
	public String login(LoginForm form, Model model) {
		//中身が1件だけのUserList
		List<User> userList = userService.login(form.getEmail(), form.getPassword());
		//セッションにカート情報がある、かつ、登録のあるユーザー情報からのログインであれば
		//ユーザー情報をセッションに入れてカート情報をDBに入れて注文画面に遷移
		if(session.getAttribute("orderItemList") != null && userList != null) {
				session.setAttribute("user", userList);
				return  "redirect:/cart/combineCart";
		}
		
		if(session.getAttribute("fromOrderHistory") != null && userList != null) {
			session.setAttribute("user", userList);
			return  "forward:/orderHistory/";
		}
		
		if(userList == null) {
			model.addAttribute("errorMessage", "メールアドレス、またはパスワードが間違っています");
			return toLogin();
		}
		session.setAttribute("user", userList);
		return "redirect:/item";
	}

	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/item";
	}
	
	
}
