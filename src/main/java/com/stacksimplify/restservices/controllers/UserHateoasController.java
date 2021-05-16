package com.stacksimplify.restservices.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stacksimplify.restservices.entities.Order;
import com.stacksimplify.restservices.entities.User;
import com.stacksimplify.restservices.exceptions.UserNotFoundException;
import com.stacksimplify.restservices.services.UserService;

@RestController
@RequestMapping(value="/hateoas/users")
@Validated
public class UserHateoasController {
    
	
	@Autowired
	private UserService userService;
	
	@GetMapping
	public CollectionModel<User> getAllUsers() throws UserNotFoundException
	{
		List<User> alluser= userService.getAllUsers();
		for(User user:alluser)
		{
			Long userId=user.getUserId();
			Link selfLink=WebMvcLinkBuilder.linkTo(this.getClass()).slash(userId).withSelfRel();
			user.add(selfLink);
			
			CollectionModel <Order> order= WebMvcLinkBuilder.methodOn(OrderHateosController.class).getAllOrders(userId);
			Link orderLink=WebMvcLinkBuilder.linkTo(order).withRel("all-orders");
			user.add(orderLink);
		}
		Link selfLinkgetAllUsers = WebMvcLinkBuilder.linkTo(this.getClass()).withSelfRel();
		CollectionModel <User> finalCollectionModel=new CollectionModel<User>(alluser,selfLinkgetAllUsers);
		return finalCollectionModel;
	}
	
	@GetMapping("/{id}")
	public EntityModel<User> getUserById(@PathVariable("id")Long id)
	{
		try {
		Optional <User> opUser= userService.getUserById(id);
		User user= opUser.get();
		Long userId=user.getUserId();
		Link selfLink=WebMvcLinkBuilder.linkTo(this.getClass()).slash(userId).withSelfRel();	
				user.add(selfLink);
		@SuppressWarnings("deprecation")
		EntityModel<User> finalEntityModel=new EntityModel<User>(user);
		return finalEntityModel;
		
		
		
		}
		catch(UserNotFoundException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
		}
	}
}
