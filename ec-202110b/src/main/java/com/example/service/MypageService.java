package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.User;
import com.example.repository.UserRepository;

@Service
@Transactional
public class MypageService {
	
	@Autowired
	private UserRepository userRepository;
	

	public void delete(Integer id) {
		userRepository.delete(id);
	}
	
	public void update(User user) {
	
		userRepository.update(user);
	}
}
