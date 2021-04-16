package com.stacksimplify.restservices.Hello;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {
	
	//@RequestMapping(method = RequestMethod.GET,path ="/helloworld")
	@GetMapping("/helloworld")
	public String helloWorld()
	{
		return "Hello World1";
		
	}
	@GetMapping("/hw-bean")
	public UserDetails helloworldbean()
	{
		return new UserDetails("Aditya","Thakur","Sarkaghat");
	}

}
