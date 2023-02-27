package com.example.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.repository.OrderRepository;

import jp.pay.Payjp;
import jp.pay.model.Charge;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private MailSender sender;
	
	
	/**ユーザーIDとステータスで注文を検索するメソッド
	 * @param userId
	 * @param status
	 * @return order
	 */
	public Order findByUserIdAndStatus(Integer userId, Integer status) {
		
		return orderRepository.findByUserIdAndStatus(userId, status);
	}
	

	/**DBを更新するメソッド
	 * @param order
	 */
	public void update(Order order) {
		orderRepository.update(order);
	}
	

	/**注文完了メールを送信するメソッド
	 * @param order
	 */
	public void sendEmail(Order order) {
		SimpleMailMessage msg = new SimpleMailMessage();		

		msg.setFrom("rak@sample.com");
		msg.setTo(order.getDestinationEmail());
		msg.setSubject("【ラクラクヌードル】注文確定のご連絡");//タイトルの設定

		String name = order.getDestinationName();
		int totalPrice = order.getCalcTotalPrice();
		Timestamp deliveryTime = order.getDeliveryTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日H時");
		String time = dateFormat.format(deliveryTime);
		
		String body = "";
		
		body += name + "様\r\n日頃よりラクラクヌードルをご利用いただきまして、誠にありがとうございます。以下の内容でご注文を承りました。\r\n\r\n【ご注文内容】";
		
		for(OrderItem orderItem : order.getOrderItemList()) {
			String itemName = orderItem.getItem().getName();
			Character size = orderItem.getSize();
			Integer quantity = orderItem.getQuantity();
			
			body += "\r\n■" + itemName + "\r\nサイズ：" + size + "\r\n数量："  + quantity + "個 \r\nトッピング:";
			
			for(OrderTopping orderTopping : orderItem.getOrderToppingList()) {
				if(orderTopping.getTopping().getName() != null) {
					String toppingName = orderTopping.getTopping().getName() + "  ";
							body += toppingName;
				}
			
		}
		body += "\r\n【合計金額】\r\n" + totalPrice + "円" + "\r\n【配達日時】\r\n" + time +
				"\r\n\r\nまたのご利用をお待ちしております。";
		
		msg.setText(body);//本文の設定
        this.sender.send(msg);
		}
	}
	
	
	
	/**pay.jpを通じてクレジット決済をするメソッド
	 * @param price　支払金額
	 * @param token　トークン化されたカード情報
	 */
	public void creditPay(int price, String token) {

		Payjp.apiKey = "";

        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", price);
        chargeMap.put("currency", "jpy");
        chargeMap.put("card", token);
        try {
            Charge charge = Charge.create(chargeMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}
