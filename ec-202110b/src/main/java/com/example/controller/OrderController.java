package com.example.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.User;
import com.example.form.OrderForm;
import com.example.service.CartService;
import com.example.service.OrderService;


/**注文をするためのコントローラー
 * @author sykn0
 *
 */
@Controller
@SpringBootApplication
@RequestMapping("/order")
public class OrderController {
	@ModelAttribute
	public OrderForm setUpOrderForm() {
		return new OrderForm();
	}
	
	@Autowired 
	private HttpSession session;
	
	@Autowired
	UserController userController;

	@Autowired
	CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	
	
	/**注文確認画面を表示させるメソッド
	 * @return　注文確認画面
	 */
	@RequestMapping("/toOrder")
	public String toOrderConfirm() {
		//ログインしていない場合ログイン画面に遷移し、ログイン後、DBに注文情報をインサートする
		List<User> userList =  (List<User>) session.getAttribute("user");
		if(userList == null) {
	  		return	 "redirect:/user/toLogin";
		}else {
			//セッションスコープが時間切れの時。セッションスコープにアイテムリストを格納しなおす。
			if(session.getAttribute("order") == null) {
				User user = userList.get(0); 
				Order order = cartService.findAllForCart(user.getId());
				session.setAttribute("orderItemList", order.getOrderItemList());
			}
			return "order/order_confirm";
		}
	}
	
	/**注文を確定させるメソッド
	 * @param form　届け出先情報、支払方法
	 * @return　注文完了画面
	 */
	@RequestMapping("/confirm")
	public String order(@Validated OrderForm form, BindingResult result, Model model) {
		
		if(result.hasErrors()) {
			if(form.getDeliveryDate() != null && form.getDeliveryTime() != null) {
			
			//配達時間エラー
			String delivery = form.getDeliveryDate() + " " + form.getDeliveryTime();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh");
	        Date parsedDate = null;
	        
	        try {
	            parsedDate = dateFormat.parse(delivery);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        Date nowDate = new Date();
	        Calendar nowCalendarDate = Calendar.getInstance();
	        nowCalendarDate.setTime(nowDate);
	        
	        nowCalendarDate.add(Calendar.HOUR_OF_DAY, 3);
	        Date deliveryOkDate = nowCalendarDate.getTime();
	        
	        if(!parsedDate.after(deliveryOkDate)) {
	        	model.addAttribute("deliveryTimeError", "今から3時間後以降の日時をご入力ください");
	        }
			}
	        
	        //カード情報エラー
	        if("1".equals(form.getPaymentMethod())) {
				if(!"".equals(form.getToken())) {
					model.addAttribute("cashPayError", "クレジットカード情報は入力しないでください");
		        	return "order/order_confirm";
				}
			}
			
			return "order/order_confirm";
		}
		
		//代引選択時にカード情報入力されていたらエラー表示
		if("1".equals(form.getPaymentMethod())) {
			if(!"".equals(form.getToken())) {
				model.addAttribute("cashPayError", "クレジットカード情報は入力しないでください");
	        	return "order/order_confirm";
			}
		}
		
		//セッションスコープからログインしている注文前のuserドメインを持ってきてユーザーIDをorderドメインにセットする
		//orderドメインにformで受け取った値をセットする。
		List<User> loginUserList =  (List<User>) session.getAttribute("user");
		User loginUser = loginUserList.get(0);
		Order order = orderService.findByUserIdAndStatus(loginUser.getId(), 0);
		order.setDestinationName(form.getDestinationName());
		order.setDestinationEmail(form.getDestinationEmail());
		order.setDestinationZipcode(form.getZipcode());
		order.setDestinationAddress(form.getAddress());
		order.setDestinationTel(form.getDestinationTel());
		order.setPaymentMethod(Integer.parseInt(form.getPaymentMethod()));
		
		order.setTotalPrice((Integer)session.getAttribute("totalPrice"));

		//orderドメインに注文時刻をセットする
		Date orderDate = new Date();
		order.setOrderDate(orderDate);
		
		
		//orderドメインのステータスを更新する
		if("1".equals(form.getPaymentMethod())) {
			order.setStatus(1);
		}else if("2".equals(form.getPaymentMethod())){
			order.setStatus(2);
		}
		
		
		//orderドメインに合計金額をセットする
		order.setTotalPrice((Integer) session.getAttribute("totalPrice"));
		
		
		//配達日時が配達可能な時刻かチェックする・配達日時をTimestampに変換してorderドメインにセットする
		
		//formでString型で受け取った配達日時を一度Date型に変換
		String delivery = form.getDeliveryDate() + " " + form.getDeliveryTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat.parse(delivery);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        //現在時刻をDate型で取得し、Calendar型に変換
        Date nowDate = new Date();
        Calendar nowCalendarDate = Calendar.getInstance();
        nowCalendarDate.setTime(nowDate);
        
        //Calendar型で現在時刻から3時間後の時刻を算出し、Calendar型からDate型に再変換
        nowCalendarDate.add(Calendar.HOUR_OF_DAY, 3);
        Date deliveryOkDate = nowCalendarDate.getTime();
        
        
        
        //formで受け取った配達時刻が現在時刻から3時間後以降かチェック
        //true → 配達日時をTimestampに変換してorderドメインにセット
        //false → エラー表示
        if(parsedDate.after(deliveryOkDate)) {
	        Timestamp timestamp = new Timestamp(parsedDate.getTime());
			order.setDeliveryTime(timestamp);
        }else {
        	model.addAttribute("deliveryTimeError", "今から3時間後以降の日時をご入力ください");
        	return "order/order_confirm";
        }
		

		//DBを更新する
		orderService.update(order);
		
		
		//メールを送信する
		List<OrderItem> itemList = (List<OrderItem>) session.getAttribute("orderItemList");
		order.setOrderItemList(itemList);
		orderService.sendEmail(order);
		
		
		//カード決済の場合pay.jpを通じて決済
		if("2".equals(form.getPaymentMethod())) {
			
			//カード情報が入力されていない時エラー表示
			if("".equals(form.getToken())) {
				model.addAttribute("creditFormError", "クレジットカード情報を入力してください");
	        	return "order/order_confirm";
			}else {
			
				orderService.creditPay(order.getTotalPrice(), form.getToken());
			}
		}

		//注文完了画面
		return "redirect:/order/toOrderFinished";
	}
	
	/**注文完了画面に遷移するメソッド
	 * @return　注文完了画面
	 */
	@RequestMapping("/toOrderFinished")
	public String toOrderFinished() {
		//注文完了後にsessionにorderItemListが残ってしまい
		//トップ画面のログインを押すとカートに商品が残った状態で注文画面に遷移してしまう
		//sessionからorderItemListだけ削除する（userだけ残ってしまう）
		session.removeAttribute("orderItemList");
		return "order/order_finished";
	}
	
}
