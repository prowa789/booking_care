package com.booking_care.controller;

import com.booking_care.model.BacSy;
import com.booking_care.repository.BacSyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class BacSyRestApi {

    @Autowired
    BacSyRepository bacSyRepo;

    @GetMapping("getAllBacSy/chuyenKhoa/{idChuyenKhoa}")
    public List<BacSy> getAllBacSy(@PathVariable("idChuyenKhoa") Integer id) {
        List<BacSy> bacSyList = bacSyRepo.getAllByChuyenKhoa(id);
        return bacSyList;
    }

    @GetMapping("/api/bacsy/{id}")
    public BacSy getBacSyApi(@PathVariable Integer id) {
        BacSy bacSy = bacSyRepo.findById(id).get();
        return bacSy;
    }

}
