package com.stacksimplify.restservices.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.stacksimplify.restservices.entities.User;
import com.stacksimplify.restservices.exceptions.UserExistsException;
import com.stacksimplify.restservices.exceptions.UserNotFoundException;
import com.stacksimplify.restservices.services.UserService;

@RestController
@RequestMapping(value="/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping
	public List<User> getAllUsers()
	{
		return userService.getAllUsers();
	}
	
	@PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user,UriComponentsBuilder builder)
    {  
		try {
    	userService.createUser(user);
    	HttpHeaders header =new HttpHeaders();
    	header.setLocation(builder.path("/users/{id}").buildAndExpand(user.getUserId()).toUri());
    	return new ResponseEntity<Void>(header,HttpStatus.CREATED);
    	
		}
		catch (UserExistsException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
    }
	
	
	@GetMapping("/{id}")
	public Optional <User> getUserById(@PathVariable("id")Long id)
	{
		try {
		return userService.getUserById(id);
		}
		catch(UserNotFoundException e)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public User getUserById(@PathVariable("id")Long id,@RequestBody User user)
	{   
		try {
		return userService.updateUserById(id, user);
		}
		catch(UserNotFoundException e)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public void deleteUserById(@PathVariable("id")Long id)
	{
		userService.deleteUserById(id);
	}
	
	
	@GetMapping("/byusername/{username}")
	public User getUserByUsername(@PathVariable("username")String username)
	{
		return userService.getUserByUsername(username);
	}
}
