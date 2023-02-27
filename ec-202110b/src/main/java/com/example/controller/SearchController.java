package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.domain.Item;
import com.example.service.ItemService;

import net.arnx.jsonic.JSON;



@Controller
@RequestMapping("/autoComplete")
public class SearchController {
	
	@Autowired
	private ItemService itemService;
	
	 @ResponseBody
	    @RequestMapping("")
	    public String getAutoComplete(){
		 	List<Item> itemList = itemService.findAll();
		 	List<String> nameList =new ArrayList();
		 	
		 	for(Item item:itemList) {
		 		nameList.add(item.getName());
		 	}
		 	
	        return JSON.encode(nameList);
	    
	}
}
