package com.echem.ecshop.controllers;

import com.echem.ecshop.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("/profile")
//    public String profileUser(Model model, Principal principal){
//
//        if (principal==null) {
//            throw new IllegalStateException("Але ж Ви не авторизовані");
//        }
//        User user = userService.findByUserName(principal.getName());
//        UserDTO dto = UserDTO.builder()
//                .username(user.getFirstName())
//                .email(user.getEmail())
//                .build();
//        model.addAttribute("user",dto);
//        return "profile";
//    }
}