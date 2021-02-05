package ru.javamentor.s_boot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javamentor.s_boot.model.User;
import ru.javamentor.s_boot.services.RoleService;
import ru.javamentor.s_boot.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

    @GetMapping(value = "/user")
	public String  userPage(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		return "user";
	}

}