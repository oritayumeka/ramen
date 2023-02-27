package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * @param user
	 * @return レポジトリーのinsertメソッドで取得したuser
	 */
	public User insert(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.insert(user);
	}
	
	/**
	 * @param email
	 * @param password
	 * @return レポジトリーのfindByEmailAndPasswordメソッドで取得したuser
	 */
	public List<User> login(String email, String password) {
		List<User> user = userRepository.findByEmail(email);
		if (user.size() == 0) {
			return null;
		}
		if (!passwordEncoder.matches(password, user.get(0).getPassword())) {
			return null;
		}
		return user;
	}
	
	/**
	 * @param email
	 * @param password
	 * @return
	 */
	public List<User> searchByEmail(String email){
		List<User> user = userRepository.findByEmail(email);
		return user;
	}
	
	

}
