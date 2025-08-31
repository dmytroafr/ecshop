package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.dto.UserDTO;
import com.echem.ecshop.service.bucket.BucketService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/buckets")
public class BucketController {

    private final BucketService bucketService;

    public BucketController(BucketService bucketService){
        this.bucketService = bucketService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public String aboutBucket(Model model, HttpSession httpSession) {
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }
        BucketDTO bucketDTO = bucketService.getBucketDtoByUserId(userDTO.id);
        model.addAttribute("bucket", bucketDTO);
        return "bucket";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{productId}/add")
    public String addToBucket(@PathVariable Long productId, Principal principal, HttpSession httpSession){
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.addBucketDetails(productId, userDTO.getId());
        return "redirect:/products";
    }


    // delete method
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{productId}/delete")
    public String deleteProduct (@PathVariable Long productId, HttpSession httpSession){

        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.deleteProductFromBucket(productId, userDTO.getId());
        return "redirect:/buckets";
    }





    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{productId}/increase")
    public String increaseProductAmount(@PathVariable Long productId, Principal principal, HttpSession httpSession) {
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");
        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.increaseProductAmount(productId, userDTO.getId());
        return "redirect:/buckets";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{productId}/decrease")
    public String decreaseProductAmount(@PathVariable Long productId, HttpSession httpSession, Principal principal) {
        if (principal==null){
            return "redirect:/login";
        }
        UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");

        if (userDTO == null) {
            return "redirect:/login";
        }
        bucketService.decreaseProductAmount(productId, userDTO.getId());
        return "redirect:/buckets";
    }

}