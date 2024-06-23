package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.SessionObjectHolder;
import com.echem.ecshop.service.bucket.BucketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/buckets")
public class BucketController {

    private final SessionObjectHolder sessionObjectHolder;
    private final BucketService bucketService;

    public BucketController(BucketService bucketService, SessionObjectHolder sessionObjectHolder){
        this.bucketService = bucketService;
        this.sessionObjectHolder = sessionObjectHolder;
    }

    @GetMapping
    public String aboutBucket(Model model, Principal principal, HttpSession httpSession){
        sessionObjectHolder.addClicks();
        if (principal==null){
            model.addAttribute("bucket", new BucketDTO());
        } else {
            UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
            if (userDTO == null) {
                return "redirect:/login";
            }
            BucketDTO bucketDTO = bucketService.getBucketDtoByUser(userDTO.id());
            model.addAttribute("bucket", bucketDTO);
        }
        return "bucket";
    }

    @GetMapping("/{productId}")
    public String addToBucket(@PathVariable Long productId, Principal principal, HttpSession httpSession){
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.addBucketDetails(productId, userDTO.id());

        return "redirect:/products";
    }

    @PostMapping ("/delete/{productId}")
    public String deleteProduct (@PathVariable Long productId, Principal principal, HttpSession httpSession){
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.deleteProductFromBucket(productId, userDTO.id());
        return "redirect:/buckets";
    }

    @PostMapping("/increase/{productId}")
    public String increaseProductAmount(@PathVariable Long productId, Principal principal, HttpSession httpSession) {
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.increaseProductAmount(productId, userDTO.id());
        return "redirect:/buckets";
    }

    @PostMapping("/decrease/{productId}")
    public String decreaseProductAmount(@PathVariable Long productId, HttpSession httpSession, Principal principal) {
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");

        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.decreaseProductAmount(productId, userDTO.id());
        return "redirect:/buckets";
    }

}