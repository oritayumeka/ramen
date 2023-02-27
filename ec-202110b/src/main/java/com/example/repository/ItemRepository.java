package com.example.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.Topping;

@Repository
public class ItemRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Item> ITEM_ROW_MAAPPER
		=new BeanPropertyRowMapper<>(Item.class);
	
	
	/**全件表示
	 * @return
	 */
	public List<Item> findAll(){
		String sql ="SELECT * FROM items ";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAAPPER);
		return itemList;
	}
	public List<Item> findAllOrderByPriceHigh(){
		String sql ="SELECT * FROM items ORDER BY price_M DESC";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAAPPER);
		return itemList;
	}
	public List<Item> findAllOrderByPriceLow(){
		String sql ="SELECT * FROM items ORDER BY price_M ";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAAPPER);
		return itemList;
	}
	public List<Item> findAllOrderByPopular(){
		
		String sql = "SELECT i.id AS id,name,description,price_m,price_l,image_path,deleted,"
				+ " COALESCE(SUM(oi.quantity),0) AS sum"
				+ " FROM orders AS o"
				+ " FULL OUTER JOIN order_items AS oi ON o.id = oi.order_id"
				+ " RIGHT OUTER JOIN items AS i ON i.id = oi.item_id"
				+ " GROUP BY i.id"
				+ " ORDER BY COALESCE(SUM(oi.quantity),0) DESC;";
				
		List<Item> itemList = template.query(sql, ITEM_ROW_MAAPPER);
		return itemList;
	}
	
	
	/**商品名で曖昧検索
	 * @param name
	 * @return
	 */
	

	public Item load(Integer id) {
		String loadSql ="SELECT "
						  + "name "
						  + ",id "
						  + ",description "
						  + ",price_m "
						  + ",price_l "
						  + ",image_path "
						  + "FROM items "
						  + "WHERE id = :id";

		SqlParameterSource param = new MapSqlParameterSource("id",id);
						  
		Item item = template.queryForObject(loadSql, param, ITEM_ROW_MAAPPER);
		return item;
	}
	
	
	private static final RowMapper<Topping> TOPPING_ROW_MAAPPER
	=new BeanPropertyRowMapper<>(Topping.class);
	
	public List<Topping> showAll(){
		String toppingSql="SELECT * FROM toppings ";
		List<Topping> toppingList = template.query(toppingSql, TOPPING_ROW_MAAPPER);
		return toppingList;
	}
	
	public List<Item> priceHigh(String name){
		String searchSql ="SELECT * FROM items WHERE name LIKE :name ORDER BY price_M DESC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
		List<Item> itemList = template.query(searchSql, param, ITEM_ROW_MAAPPER);
		return itemList;
	}
	public List<Item> priceLow(String name){
		String searchSql ="SELECT * FROM items WHERE name LIKE :name ORDER BY price_M";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
		List<Item> itemList = template.query(searchSql, param, ITEM_ROW_MAAPPER);
		return itemList;
	}
	public List<Item> orderBypopular(String name){
		
		String searchSql = "SELECT i.id AS id,name,description,price_m,price_l,image_path,deleted,"
				+ "COALESCE(SUM(oi.quantity),0) AS sum FROM orders AS o"
				+ " FULL OUTER JOIN order_items AS oi ON o.id = oi.order_id"
				+ " RIGHT OUTER JOIN items AS i ON i.id = oi.item_id"
				+ " WHERE name LIKE :name"
				+ " GROUP BY i.id"
				+ " ORDER BY COALESCE(SUM(oi.quantity),0) DESC;";
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
		List<Item> itemList = template.query(searchSql, param, ITEM_ROW_MAAPPER);
		return itemList;
	}

	//--------------------Cartで使用----------------------
	//Toppingの個別検索
	public Topping loadForTopping(Integer id) {
		String loadSql ="SELECT * FROM toppings WHERE id = :id";

		SqlParameterSource param = new MapSqlParameterSource("id",id);
						  
		Topping topping = template.queryForObject(loadSql, param,TOPPING_ROW_MAAPPER);
		return topping;
	}
	
	//人気Top5商品検索(カート表示用)
	public List<Item> topFivepopularItems() {
		
		String searchSql ="SELECT i.id AS id,name,description,price_m,price_l,image_path,deleted,SUM(oi.quantity) AS sum"
				+ " FROM orders AS o"
				+ " FULL OUTER JOIN order_items AS oi ON o.id = oi.order_id"
				+ " JOIN items AS i ON i.id = oi.item_id"
				+ " WHERE o.status NOT IN (0,9)"
				+ " GROUP BY i.id"
				+ " ORDER BY SUM(oi.quantity) DESC"
				+ " LIMIT 5;";
		
		List<Item> topFivepopularItems = template.query(searchSql,  ITEM_ROW_MAAPPER);
		return topFivepopularItems;
	}
	
	//人気Top10版（総合ランキング用）
	public List<Item> topTenpopularItems() {
		
		String searchSql ="SELECT i.id AS id,name,description,price_m,price_l,image_path,deleted,SUM(oi.quantity) AS sum"
				+ " FROM orders AS o"
				+ " FULL OUTER JOIN order_items AS oi ON o.id = oi.order_id"
				+ " JOIN items AS i ON i.id = oi.item_id"
				+ " WHERE o.status NOT IN (0,9)"
				+ " GROUP BY i.id"
				+ " ORDER BY SUM(oi.quantity) DESC"
				+ " LIMIT 10;";
		
		List<Item> topTenpopularItems = template.query(searchSql,  ITEM_ROW_MAAPPER);
		return topTenpopularItems;
	}
	
	
		/**
		 * 人気トップ10版（月間ランキング用）
		 * @param orderMonth1　始期の年
		 * @param orderDay1　始期の月
		 * @param orderMonth2　終期の年
		 * @param orderDay2　終期の月
		 * @return
		 */
		public List<Item> topTenpopularItemsByMonth(Timestamp orderDate1,Timestamp orderDate2) {
			
			String searchSql ="SELECT i.id AS id,name,description,price_m,price_l,image_path,deleted,SUM(oi.quantity) AS sum"
					+ " FROM orders AS o"
					+ " FULL OUTER JOIN order_items AS oi ON o.id = oi.order_id"
					+ " JOIN items AS i ON i.id = oi.item_id"
					+ " WHERE o.status NOT IN (0,9) AND (o.order_date >=:orderDate1 And o.order_date < :orderDate2)"
					+ " GROUP BY i.id"
					+ " ORDER BY SUM(oi.quantity) DESC"
					+ " LIMIT 10;";
			
			SqlParameterSource param = new MapSqlParameterSource().addValue("orderDate1",orderDate1)
						.addValue("orderDate2",orderDate2);
			List<Item> topTenpopularItemsByMonth = template.query(searchSql,param,ITEM_ROW_MAAPPER);
			return topTenpopularItemsByMonth;
		}
	
}
