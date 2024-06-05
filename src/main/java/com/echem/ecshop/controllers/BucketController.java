package com.echem.ecshop.controllers;

import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.service.BucketService;
import com.echem.ecshop.service.SessionObjectHolder;
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
    public String aboutBucket(Model model, Principal principal){
        sessionObjectHolder.addClicks();

        if (principal==null){
            model.addAttribute("bucket", new BucketDTO());
        } else {
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDTO);
        }
        return "bucket";
    }

        @GetMapping("/{productId}")
    public String addToBucket(@PathVariable Long productId, Principal principal){
            sessionObjectHolder.addClicks();

        if (principal==null){
            return "redirect:/login";
        }
        bucketService.addBucketDetails(productId, principal.getName());
        return "redirect:/products";
    }

    @PostMapping ("/{productId}")
    public String deleteProduct (@PathVariable Long productId, Model model, Principal principal){
        if (principal==null){
            return "redirect:/login";
        }
        BucketDTO bucketDTO = bucketService.deleteProductFromBucket(productId, principal.getName());
        model.addAttribute("bucket", bucketDTO);
        return "redirect:/buckets";
    }


}