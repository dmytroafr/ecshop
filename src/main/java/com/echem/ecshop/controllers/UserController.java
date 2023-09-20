package com.echem.ecshop.controllers;
import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	private final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	@GetMapping
	public String userList (Model model){
		model.addAttribute("users", userService.getAll());
		return "userlist";
	}
	@GetMapping("/new")
	public String newUser(Model model){
		logger.info("Отримали запит на /new і перевели на user.html");

		 model.addAttribute ("user", new UserDTO());

		 return "user";
	}

	@PostMapping("/new")
	public String saveUser(UserDTO dto, Model model){

		if (userService.save(dto)){

			return "redirect:/users";

		} else {
			model.addAttribute("user", dto);
			return "user";
		}
	}
	@PostAuthorize("isAuthenticated() and #username == authentication.principal.username")
	@GetMapping("/{name}/roles")
	@ResponseBody
	public String getRoles(@PathVariable("name") String username){
		System.out.println("Called method getRoles");
		User byName = userService.findByName(username);
		return byName.getRole().name();
	}

	@GetMapping("/profile")
	public String profileUser(Model model, Principal principal){

		if (principal==null) {
			throw new RuntimeException("Але ж Ви не авторизовані");
		}
		User user = userService.findByName(principal.getName());
		UserDTO dto = UserDTO.builder()
				.username(user.getName())
				.email(user.getEmail())
				.build();
		model.addAttribute("user",dto);
		return "profile";
	}
	@PostMapping("/profile")
	public String updateProfileUser (UserDTO dto, Model model, Principal principal){
		if (principal == null || Objects.equals(principal.getName(),dto.getUsername())){
			throw new RuntimeException("Але ж Ви не авторизовані");
		}
		if (dto.getPassword() != null
				&& !dto.getPassword().isEmpty()
				&& !Objects.equals(dto.getPassword(),dto.getMatchingPassword())){
			model.addAttribute("user",dto);
			return "profile";
		}
		userService.updateProfile(dto);
		return "redirect:users/profile";
	}
}
