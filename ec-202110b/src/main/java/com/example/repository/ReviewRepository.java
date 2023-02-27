package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Review;

/**
 * レビュー投稿用レポジトリ
 * 1129実装中
 * @author igayuki
 *
 */
@Repository
public class ReviewRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Review> REVIEW_ROW_MAPPER = new BeanPropertyRowMapper<>(Review.class);
	
	/**
	 * レビューに追加するメソッド
	 * @param review 追加されたレビューのドメイン
	 */
	public void insert(Review review) {
		
		String sql = "INSERT INTO reviews(name,review,user_id,item_id) VALUES (:name,:review,:userId,:itemId)";
		SqlParameterSource param = new BeanPropertySqlParameterSource(review);
		template.update(sql, param);
		
	}
	
	/**
	 * レビューを検索するメソッド
	 * @param itemId 検索対象の商品のID
	 * @return 対象商品のレビューリスト
	 */
	public List<Review> showReviewByItemId(Integer itemId) {
		
		String sql = "SELECT * FROM reviews WHERE item_id = :itemId ORDER BY id DESC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		return template.query(sql, param, REVIEW_ROW_MAPPER);
	}
}
