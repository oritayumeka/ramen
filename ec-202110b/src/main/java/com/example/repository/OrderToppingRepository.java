package com.example.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.OrderTopping;

/**
 * トッピング注文用レポジトリ
 * @author igayuki
 *
 */
@Repository
public class OrderToppingRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<OrderTopping> ORDERTOPPING_ROW_MAPPER
	=(rs,i) -> {
		OrderTopping orderTopping = new OrderTopping();
		
		orderTopping.setId(rs.getInt("id"));
		
		orderTopping.setToppingId(rs.getInt("topping_id"));
		
		orderTopping.setOrderItemId(rs.getInt("order_item_id"));
		
		return orderTopping;
	};
	

	/**
	 * カートにトッピングを追加した際の登録メソッド
	 * @param orderTopping　カートに追加されたトッピング
	 */
	public void insert(OrderTopping orderTopping) {
		
		String sql = "INSERT INTO order_toppings(topping_id,order_item_id) "
				+ "VALUES(:toppingId,:orderItemId)";
		
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
		
		template.update(sql, param);
	}
	
	/**
	 * カートから商品を削除した際のトッピングDBでの削除処理メソッド
	 * @param id　カートから削除された商品ID
	 */
	public void delete(Integer orderItemId) {
		
		String sql = "DELETE FROM order_toppings WHERE order_item_id = :orderItemId;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId",orderItemId);
		
		template.update(sql, param);
		
	}

}
