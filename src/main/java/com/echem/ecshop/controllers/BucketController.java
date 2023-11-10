package com.echem.ecshop.controllers;

import com.echem.ecshop.domain.Bucket;
import com.echem.ecshop.dto.BucketDTO;
import com.echem.ecshop.service.BucketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/bucket")
public class BucketController {
    private final BucketService bucketService;
    public BucketController(BucketService bucketService){
        this.bucketService = bucketService;
    }
    @GetMapping
    public String aboutBucket(Model model, Principal principal){
        if (principal==null){
            model.addAttribute("bucket", new BucketDTO());
        } else {
            BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
            model.addAttribute("bucket", bucketDTO);
        }
        return "bucket";
    }
    @PostMapping ("/{productId}")
    public String deleteProduct (@PathVariable Long productId, Model model, Principal principal){

        BucketDTO bucketDTO = bucketService.getBucketByUser(principal.getName());
        bucketService.deleteProductFromBucket(bucketDTO,productId);
        model.addAttribute("bucket",bucketDTO);

        return "redirect:/bucket";
    }


}