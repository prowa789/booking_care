package com.booking_care.controller;

import com.booking_care.model.BaiDang;
import com.booking_care.model.TaiKhoan;
import com.booking_care.repository.BaiDangRepository;
import com.booking_care.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/bai-dang")
public class BaiDangController {

    @Autowired
    BaiDangRepository baiDangRepo;

    @ModelAttribute
    TaiKhoan taiKhoan() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof CustomUserDetails && ((CustomUserDetails) user).hasRole("BENH_NHAN")) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getTaiKhoan();
        }
        return null;
    }

    @GetMapping
    public String getAllBaiDang(Model model) {
        List<BaiDang> baiDangList = baiDangRepo.findAll();
        model.addAttribute("baiDangList", baiDangList);
//        System.out.println(baiDangList);
        return "blog";
    }

    @GetMapping("{id}")
    public String getBaiDang(@PathVariable Integer id, Model model) {
        BaiDang baiDang = baiDangRepo.findById(id).get();
        model.addAttribute("baiDang", baiDang);
//        System.out.println(baiDangList);
        return "blog-detail";
    }
}
