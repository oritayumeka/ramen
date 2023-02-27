package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.domain.User;

@Repository
public class UserRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	public static final RowMapper<User> USER_ROW_MAPPER = (rs, i) ->{
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		return user;
	};
	
	
	/**
	 * ユーザーを登録するinsertメソッド
	 * ※自動採番されたidもuserに含める
	 * @param user
	 * @return user
	 */
	public User insert(User user) {
		String sql = "INSERT INTO users (name, email, password, zipcode, address, telephone) VALUES (:name, :email, :password, :zipcode, :address, :telephone)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = {"id"};
		template.update(sql, param, keyHolder, keyColumnNames);
		user.setId(keyHolder.getKey().intValue());
		return user;
	}
	
	/**
	 * メールアドレスで一件検索するfindByEmailメソッド
	 * ※パスワードはハッシュ化
	 * @param email
	 * @param password
	 * @return user
	 */
	public List<User> findByEmail(String email) {
		String sql = "SELECT * FROM users WHERE email=:email;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email",email);
		List<User> user = template.query(sql, param, USER_ROW_MAPPER);
		return user;
	}
	

	/**
	 * 退会用のユーザー情報を削除するメソッド
	 * @param id
	 */
	public void delete(Integer id) {
		String sql = "DELETE FROM users WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id",id);
		template.update(sql, param);
	}
	
	/**
	 * 登録済のユーザー情報を変更するメソッド
	 * @param user
	 */
	public void update(User user) {
		String sql = "UPDATE users "
				+ "SET name = :name, email = :email, zipcode = :zipcode, address = :address, telephone = :telephone "
				+ "WHERE id=:id;";
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		template.update(sql, param);
	}
	
	


}
