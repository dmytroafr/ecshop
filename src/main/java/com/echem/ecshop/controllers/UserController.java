package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.User;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public String userList (Model model){
        model.addAttribute("users", userService.getAll());
        return "userList";
    }
    @GetMapping("/new")
    public String newUser(Model model){
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

        if (principal == null || !Objects.equals(principal.getName(),dto.getUsername())){
            throw new RuntimeException("Але ж Ви не авторизовані");
        }
        model.addAttribute("pasMatch",true);
        model.addAttribute("haveChanged",false);

        if (Objects.equals(dto.getPassword(),dto.getMatchingPassword())){
            model.addAttribute("user",dto);
            model.addAttribute("haveChanged",true);
            userService.updateProfile(dto);
            return "profile";
        }
        model.addAttribute("pasMatch",false);
        return "profile";
    }
}