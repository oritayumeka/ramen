package com.example.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;
import com.example.domain.User;

@Repository
public class OrderHistoryRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Order> ORDER_ROW_MAPPER = new BeanPropertyRowMapper<>(Order.class);
	
	private static final ResultSetExtractor<List<Order>> ORDER_EXTRACTOR
	=(rs) -> {
		
		List<Order> orderList = new ArrayList();
		
		//orderの被りチェックするMapを用意→削除予定becauseSQL上orderには被りはない
		//order(orderId)はあるかないかの２択
		Map<Integer,Order> orderMap = new LinkedHashMap();
		//orderItemの被りチェックするMapを用意
		Map<Integer,OrderItem> orderItemMap = new LinkedHashMap();
		
		Order order = null;
		OrderItem orderItem = null;
		while(rs.next()) {
			//Orderの中身入れる！！！！
			Integer orderId = rs.getInt("o_id");
			order = orderMap.get(orderId);
			
			if(order == null){
				order = new Order();
				order.setId(rs.getInt("o_id"));
				order.setUserId(rs.getInt("user_id"));
				order.setStatus(rs.getInt("status"));
				order.setTotalPrice(rs.getInt("total_price"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setDestinationName(rs.getString("destination_name"));
				order.setDestinationEmail(rs.getString("destination_email"));
				order.setDestinationZipcode(rs.getString("destination_zipcode"));
				order.setDestinationAddress(rs.getString("destination_address"));
				order.setDestinationTel(rs.getString("destination_tel"));
				order.setDeliveryTime(rs.getTimestamp("delivery_time"));
				order.setPaymentMethod(rs.getInt("payment_method"));
				
				//Userをnew→中身を入れてOrderにSet
				User user = new User();
				user.setId(rs.getInt("u_id"));
				user.setName(rs.getString("u_name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				user.setZipcode(rs.getString("zipcode"));
				user.setAddress(rs.getString("address"));
				user.setTelephone(rs.getString("telephone"));
				
				order.setUser(user);
				
				//orderの被りチェックするMapに入れる
				orderMap.put(orderId, order);
				order.setOrderItemList(new ArrayList());
			}
			
			//OrderItemの中身入れる
			//中でItemをnewしていれる(価格の確定はServiceで行う→サイズとサイズ毎の価格はドメインが情報を持っている)
			
			Integer orderItemId = rs.getInt("oi_id");
			orderItem = orderItemMap.get(orderItemId);
			
			if(orderItem == null) {
				orderItem = new OrderItem();
				orderItem.setId(orderItemId);
				orderItem.setItemId(rs.getInt("item_id"));
				orderItem.setOrderId(rs.getInt("order_id"));
				orderItem.setQuantity(rs.getInt("quantity"));
				//sizeは型の変換を行う
				Character size = rs.getString("size").charAt(0);
				orderItem.setSize(size);
				
				//Itemドメインをnewして中身をDBからのデータで詰める
				//そのドメインをOrderItemドメインのフィールドに詰める
				Item item = new Item();
				item.setId(rs.getInt("i_id"));
				item.setName(rs.getString("i_name"));
				item.setDescription(rs.getString("description"));
				item.setPriceM(rs.getInt("i_price_m"));
				item.setPriceL(rs.getInt("i_price_l"));
				item.setImagePath(rs.getString("image_path"));
				item.setDeleted(rs.getBoolean("deleted"));
				//ItemドメインにはtoppingListだけ入ってないことに注意！
				
				orderItem.setItem(item);
				
				//orderItemの被りチェックするMapに入れる
				orderItemMap.put(orderItemId, orderItem);
				
				//orderItemのOrderToppingListを用意
				orderItem.setOrderToppingList(new ArrayList());
				
				//orderのorderItemListにorderItemを詰める
				order.getOrderItemList().add(orderItem);
				
			}
			
			//OrderTopping入れる
			//OrderToppingの中でToppingをnewしていれる(価格の確定はServiceで行う→サイズとサイズ毎の価格はドメインが情報を持っている)
			
			Integer orderToppingId = rs.getInt("ot_id");
			if(orderToppingId != null) {
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setId(orderToppingId);
				orderTopping.setToppingId(rs.getInt("topping_id"));
				orderTopping.setOrderItemId(rs.getInt("order_item_id"));
				
				//Toppingドメインをnewして中身をDBからのデータで詰める
				//そのドメインをOrderToppingドメインのフィールドに詰める
				Topping topping = new Topping();
				topping.setId(rs.getInt("t_id"));
				topping.setName(rs.getString("t_name"));
				topping.setPriceM(rs.getInt("t_price_m"));
				topping.setPriceL(rs.getInt("t_price_l"));
				
				orderTopping.setTopping(topping);
				
				//orderItemのorderToppingListにorderToppingを詰める
				orderItem.getOrderToppingList().add(orderTopping);
				
			}
			
		}
		
		
		if(orderMap.size() == 0) {
			return null;
			
		}
		
		return new ArrayList<Order>(orderMap.values());
		
		
	};
	
	/**
	 * 注文情報検索用メソッド（注文履歴とか状態確認での使用を想定）
	 * @param userId 履歴を検索する対象ユーザー
	 * @param 注文確定されて情報が完備されたOrder情報
	 * @return
	 */
		public List<Order> findOrderHistory(Integer userId) {
		
		String sql = "SELECT o.id AS o_id,o.user_id,o.status,o.total_price,order_date,destination_name,destination_email,destination_zipcode,"
				+ "destination_address,destination_tel,delivery_time,payment_method,"
				+ "u.id AS u_id,u.name AS u_name,email,password,zipcode,address,telephone,"
				+ "oI.id AS oI_id,oI.item_id,oI.order_id,oI.quantity,"
				+ "oI.size,i.id AS i_id,i.name AS i_name,i.description,i.price_m AS i_price_m,i.price_l AS i_price_l,i.image_path,i.deleted,"
				+ "oT.id AS oT_id,oT.topping_id,oT.order_item_id,t.id AS t_id,t.name AS t_name,t.price_m AS t_price_m,t.price_l AS t_price_l"
				+ " FROM orders AS o"
				+ " JOIN users AS u ON o.user_id = u.id"
				+ " FULL OUTER JOIN order_items AS oI ON o.id = oI.order_id"
				+ " JOIN items AS i ON i.id = oI.item_id"
				+ " FULL OUTER JOIN order_toppings AS oT ON oI.id = oT.order_item_id"
				+ " FULL OUTER JOIN toppings AS t ON T.id = oT.topping_id"
				+ " WHERE o.user_id = :userId And o.status <> 0 ORDER BY o_id DESC;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId",userId);
		
		return template.query(sql, param, ORDER_EXTRACTOR);
		}
		
		public List<Order> findOrderHistoryByOne(Integer orderId){
			
			String sql = "SELECT o.id AS o_id,o.user_id,o.status,o.total_price,order_date,destination_name,destination_email,destination_zipcode,"
					+ "destination_address,destination_tel,delivery_time,payment_method,"
					+ "u.id AS u_id,u.name AS u_name,email,password,zipcode,address,telephone,"
					+ "oI.id AS oI_id,oI.item_id,oI.order_id,oI.quantity,"
					+ "oI.size,i.id AS i_id,i.name AS i_name,i.description,i.price_m AS i_price_m,i.price_l AS i_price_l,i.image_path,i.deleted,"
					+ "oT.id AS oT_id,oT.topping_id,oT.order_item_id,t.id AS t_id,t.name AS t_name,t.price_m AS t_price_m,t.price_l AS t_price_l"
					+ " FROM orders AS o"
					+ " JOIN users AS u ON o.user_id = u.id"
					+ " FULL OUTER JOIN order_items AS oI ON o.id = oI.order_id"
					+ " JOIN items AS i ON i.id = oI.item_id"
					+ " FULL OUTER JOIN order_toppings AS oT ON oI.id = oT.order_item_id"
					+ " FULL OUTER JOIN toppings AS t ON T.id = oT.topping_id"
					+ " WHERE o.id = :orderId";
			
			SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);
			
			return template.query(sql, param, ORDER_EXTRACTOR);
			
		}
	
}
