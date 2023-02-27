package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.OrderItem;


/**
 * 商品注文用レポジトリ
 * @author igayuki
 *
 */
@Repository
public class OrderItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<OrderItem> ORDERITEM_ROW_MAPPER
	=(rs,i) -> {
		OrderItem orderItem = new OrderItem();
		
		orderItem.setId(rs.getInt("id"));
		
		orderItem.setItemId(rs.getInt("item_id"));
		
		orderItem.setOrderId(rs.getInt("order_id"));
		
		orderItem.setQuantity(rs.getInt("quantity"));
		
		//order_itemテーブルでsizeはvarchar(1)型
		//OrderItemドメインでsizeはCharacter型
		//そのためDBからStringで取り出してchar型に変換後、ドメインにsetしている
		Character size = rs.getString("size").charAt(0);
		
		orderItem.setSize(size);
		
		return orderItem;
	};
	
	

	
	
	/**
	 * カートに商品を追加した際の登録メソッド
	 * @param orderItem　カートに追加された商品
	 */
	public void insert(OrderItem orderItem) {
		
		String sql = "INSERT INTO order_items(item_id,order_id,quantity,size) "
				+ "VALUES(:itemId,:orderId,:quantity,:size);";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		
		template.update(sql, param);
	}
	
	
	/**
	 *  カートに商品を入れる際に必要となるOrderItemのID（を含むオブジェクト）を検索するメソッド
	 * @param orderId　検索の際に必要となるOrderドメインのID
	 * @return　OrderItemオブジェクト
	 */
	public OrderItem searchOrderItemForCart(Integer orderId) {
		
		String sql = "SELECT * FROM order_items WHERE order_id = :orderId "
				+ "ORDER BY id DESC LIMIT 1;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId",orderId);
		
		OrderItem orderItem = template.queryForObject(sql, param,ORDERITEM_ROW_MAPPER);
		
		return orderItem;
	}
	
	
	/**
	 * カートから商品を削除した際のメソッド
	 * @param id　カートから削除された商品ID
	 */
	public void delete(Integer id) {
		
		String sql = "DELETE FROM order_items WHERE id = :id;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);
		
		template.update(sql, param);
		
	}
	
	

}
